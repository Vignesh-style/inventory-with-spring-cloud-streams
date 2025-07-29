package in.co.sa.inventory.online.listener;

import com.google.gson.Gson;

import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.config.InventoryConfig;
import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component("L3VPNInventoryChangeEventListener")
public class L3VPNInventoryChangeEventListener extends InventoryFWEventsCollector implements BeanNameAware
{
    @Autowired
    ApplicationContext context;

    @Autowired
    InventoryConfig inventoryConfig;

    private static final Logger logger = LogManager.getLogger();
    private final StreamBridge streamBridge;
    private String beanName;

    public L3VPNInventoryChangeEventListener(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @KafkaListener(id = "RcaL3VPNVRFEndDataBuilderListener", topics = "NonMOTelecomObject")
    public void eventOccurred(ConsumerRecord<String, Object> record, Acknowledgment ack) throws Exception
    {
        try
        {
            Object value = record.value();

            String valueString = (String) value;
            Map<String, Object> valueMap = new Gson().fromJson(valueString, Map.class);

            String name = (String) valueMap.get("Name");
            String id = (String) valueMap.get("Id");
            String circuitId = (String) valueMap.get("circuitId");

            InventoryOperationType operationType = InventoryOperationType.ADD;

            L3VPNVRFEnd l3VPNVRFEnd = new L3VPNVRFEnd();
            l3VPNVRFEnd.setInterfaceName(name);
            l3VPNVRFEnd.setId(Long.parseLong(id));
            l3VPNVRFEnd.setCircuitId(circuitId);

            NameIDType nit = new NameIDType(l3VPNVRFEnd.getInterfaceName(), l3VPNVRFEnd.getId(), "L3VPNVRFEnd");

            List<String> builders = inventoryConfig.getListenerToBuilderMap().get(beanName);
            Map<String, InventoryDataBuilderConfig> builderConfigurations = inventoryConfig.getBuilderConfigurations();

            List<InventoryDataBuilderConfig> builderConfigs = new ArrayList<>();
            for (String builder : builders) {
                InventoryDataBuilderConfig inventoryDataBuilderConfig = builderConfigurations.get(builder);
                builderConfigs.add(inventoryDataBuilderConfig);
            }

            submitInput(new BuilderQueueInput(operationType, Collections.singletonList(new InventoryEventInput(nit, l3VPNVRFEnd)), 1), builderConfigs, streamBridge);
            ack.acknowledge();
        }
        catch (Exception e)
        {
            logger.error("Exception", e);
        }

    }

    private InventoryOperationType getInventoryOperationType(DAOEventType type)
    {
        if (type == DAOEventType.ADD)
            return InventoryOperationType.ADD;
        else if (type == DAOEventType.DELETE)
            return InventoryOperationType.DELETE;
        else if (type == DAOEventType.UPDATE)
            return InventoryOperationType.UPDATE;

        return null;
    }

    @Override
    public List<BuilderQueueInput> getBuilderQueueInputForBatchEvent(List<GenericEvent> events) {
        return null;
    }


    @Override
    public boolean isBatchRequired() {
        return false;
    }

    @Override
    public void registerListener(Element customInput) {

    }

    @Override
    public void shutdownListener() {

    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
