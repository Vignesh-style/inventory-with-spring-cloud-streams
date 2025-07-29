package in.co.sa.inventory.gate;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.co.sa.inventory.commons.InventoryDataBuilderQueueObject;
import in.co.sa.inventory.commons.QueueObject;
import in.co.sa.inventory.data.builder.Reconcilable;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Component
public class Gatekeeper {

    private static final Logger log = LogManager.getLogger(Gatekeeper.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    ObjectMapper objectMapper;

    private final StreamBridge streamBridge;

    private final Map<String, ConcurrentLinkedQueue<QueueObject>> builderBufferMap = new ConcurrentHashMap<>();

    public Gatekeeper(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostConstruct
    public void initFlushExecutor() {
        ScheduledExecutorService flushExecutor = Executors.newScheduledThreadPool(1);
        flushExecutor.scheduleAtFixedRate(this::flushBufferedEvents, 30, 10, TimeUnit.SECONDS);
    }

    @Bean
    public Consumer<QueueObject> gatekeeperIn0() {
        return msg -> {
            log.info("Received from RECON: {}", msg);

            MessageHeaders headers = msg.getHeaders();

            String type = (String) headers.get("type"); // custom header set by producer
            log.info("Received type: {} vs : {}", type, InventoryDataBuilderQueueObject.class.getSimpleName());


            if (InventoryDataBuilderQueueObject.class.toString().equals(type)) {

                InventoryDataBuilderQueueObject obj = null;
                try {
                    obj = objectMapper.readValue(msg.getPayload(), InventoryDataBuilderQueueObject.class);
                } catch (IOException e) {
                    log.error("exception while unarchiving payload : {}", obj, e);
                }

                log.info("InventoryDataBuilderQueueObject {}", obj);
                if (obj == null) return;

                String builderBeanName = obj.builderInformation.getBuilderBeanName();
                Reconcilable builder = applicationContext.getBean(builderBeanName, Reconcilable.class);

                if (!builder.isReconciliationStateActive()) {
                    log.error("Reconciliation not running for builder: {}", builderBeanName);
                    return;
                }
                log.info("streamBridge send {}", msg);
                streamBridge.send("gatekeeper-out-0", msg);

                // Once reconciliation completes, deactivate
                builder.deactivateReconciliation();

            }

        };
    }

    @Bean
    public Consumer<QueueObject> gatekeeperIn1() {
        return msg -> {

            log.info("Received from ONLINE: {}", msg);

            InventoryDataBuilderQueueObject streamPayload = (InventoryDataBuilderQueueObject) msg;
            String builderBeanName = streamPayload.builderInformation.getBuilderBeanName();
            Reconcilable builder = applicationContext.getBean(builderBeanName, Reconcilable.class);

            if (builder.isReconciliationStateActive()) {
                // Buffer event
                builderBufferMap.computeIfAbsent(builderBeanName, b -> new ConcurrentLinkedQueue<>()).add(msg);
                log.info("Buffered online event for builder {} due to active reconciliation", builderBeanName);
                return;
            }

            streamBridge.send("gatekeeper-out-0", msg);
        };
    }

    private void flushBufferedEvents() {

        builderBufferMap.forEach((builderBeanName, queue) -> {

            Reconcilable builder = applicationContext.getBean(builderBeanName, Reconcilable.class);

            if (!builder.isReconciliationStateActive() && !queue.isEmpty()) {
                log.info("Flushing {} buffered events for builder {}", queue.size(), builderBeanName);

                QueueObject event;
                while ((event = queue.poll()) != null) {
                    streamBridge.send("gatekeeper-out-0", event);
                }
            }
        });
    }
}
