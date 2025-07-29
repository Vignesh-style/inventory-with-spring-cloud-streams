package in.co.sa.inventory.recon;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.config.InventoryConfig;
import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.config.entities.ObjectTypeNdSize;
import in.co.sa.inventory.config.entities.ReconcilerConfig;
import in.co.sa.inventory.data.builder.Reconcilable;
import in.co.sa.inventory.reconcilers.ReconData;
import in.co.sa.inventory.reconcilers.ReconInput;
import in.co.sa.inventory.reconcilers.ReconResult;
import in.co.sa.inventory.reconcilers.Reconciler;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MasterReconService {

    @Autowired
    InventoryConfig inventoryConfig;

    @Autowired
    ApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(MasterReconService.class);
    private final StreamBridge streamBridge;

    public MasterReconService(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }


    public void doRecon(String reconMode, List<String> specifications) {

        log.info("reconMode : {} & specifications : {}", reconMode, specifications);

        if (reconMode.equals("FULL_RECON")) {
            doFullRecon();
            return;
        }

        proceedReconciliation(specifications);
    }

    private void doFullRecon() {

        Map<String, List<ReconcilerConfig>> builderToReconcilerConfigs = inventoryConfig.getBuilderToReconcilerConf();
//        Set<ReconcilerConfig> reconcilerConfigs = builderToReconcilerConfigs.values().stream().flatMap(Collection::stream).filter(ReconcilerConfig::isReconEnabled).collect(Collectors.toSet());

        log.info("doFullRecon --> launching builders :: {}", builderToReconcilerConfigs.keySet());

        proceedReconciliation(builderToReconcilerConfigs.keySet());

    }

    private void proceedReconciliation(Collection<String> builderSpecifications) {

        for (String spec : builderSpecifications) {

            System.out.println(spec);

            InventoryDataBuilderConfig builderConfig = inventoryConfig.getBuilderConfigurations().get(spec);
            Reconcilable builder = context.getBean( builderConfig.getBuilderBeanName(), Reconcilable.class);

            if (builder.isReconciliationStateActive()){
                log.info("Already Recon is Active for the : {}, Hence not proceeding for this builder", spec);
                continue;
            }

            builder.activeReconciliation();

            List<ReconcilerConfig> reconcilers = inventoryConfig.getBuilderToReconcilerConf().get(spec);
            System.out.println(reconcilers);
            List<ObjectTypeNdSize> objectTypeSequence = builderConfig.getObjectTypeSequence();

            for (ReconcilerConfig rec : reconcilers) {

                Reconciler reconcilerBean = (Reconciler) context.getBean(rec.getBeanName());
                ReconResult reconResult = reconcilerBean.doRecon(new ReconInput(rec.isRetriggerFailedObjects()));

                if (reconResult == null) {
                    log.info("ReconResult is null for Reconciler :{}", rec);
                    continue;
                }

                log.info("reconResult :: {} for the rec : {}", reconResult, rec);

                for (ObjectTypeNdSize obj : objectTypeSequence) {

                    String objectType = obj.getType();
                    int batchSize = obj.getBatchSize();

                    log.info("ObjectType:{},BatchSize:{}", objectType, batchSize);

                    ReconData reconData = reconResult.getReconData(objectType);

                    if (reconData == null) {
                        log.info("No recon data obtained for object type :{}", objectType);
                        continue;
                    }

                    log.info("ObjectType:{},Add:{},Update:{},Delete:{}", objectType, reconData.isAddListEmpty() ? 0 : reconData.getAddList().size(),
                            reconData.isUpdateListEmpty() ? 0 : reconData.getUpdateList().size(), reconData.isDeleteListEmpty() ? 0 : reconData.getDeleteList().size());

                    if (!reconData.isDeleteListEmpty())
                        addReconEventToInventoryDataBuilderQueue(new BuilderQueueInput(InventoryOperationType.DELETE, reconData.getDeleteList(), batchSize), builderConfig);
                    if (!reconData.isUpdateListEmpty()) {
                        BuilderQueueInput builderQueueInput = new BuilderQueueInput(InventoryOperationType.UPDATE, batchSize);
                        List<ReconUpdateInventoryEventInput> updateList = reconData.getUpdateList();
                        for (ReconUpdateInventoryEventInput reconUpdateInventoryEventInput : updateList)
                            builderQueueInput.addEvent(reconUpdateInventoryEventInput);

                        addReconEventToInventoryDataBuilderQueue(builderQueueInput, builderConfig);
                    }
                    if (!reconData.isAddListEmpty())
                        addReconEventToInventoryDataBuilderQueue(new BuilderQueueInput(InventoryOperationType.ADD, reconData.getAddList(), batchSize), builderConfig);

//                    reconciler.reconEventsSubmitted(objectType, dbMaster);
//                    masterBuilderService.addPostReconInvokerCommand(reconciler, objectType);
                }
            }

            log.info("All ObjectSequence events added..");
                
            }
        }

    private void addReconEventToInventoryDataBuilderQueue(BuilderQueueInput builderQueueInput, InventoryDataBuilderConfig builderConfig) {

        addInBatchToBuilderQueue(builderQueueInput, builderQueueInput.getBatchSize(), true, builderConfig);
    }

    private void addInBatchToBuilderQueue(BuilderQueueInput builderQueueInput, int batchSize, boolean isReconEvent, InventoryDataBuilderConfig builderConfig) {

        List<InventoryEventInput> events = builderQueueInput.getEvents();
        log.info("builderQueueInputSize==={} batch==={} isRecon==={} ", events.size(), batchSize, isReconEvent);
        int begin = 0;
        int end = Math.min(batchSize, events.size());
        int count = 0;

        List<InventoryEventInput> obj = new ArrayList<>(events);
        do {
            count++;
            List<InventoryEventInput> subList = new ArrayList<>(obj.subList(begin, end));
            addInBuilderQueue(new InventoryDataBuilderQueueObject(new BuilderQueueInput(builderQueueInput.getOperationType(), subList, builderQueueInput.getBatchSize()), isReconEvent, builderConfig));
            begin = end;
            end = Math.min(end + batchSize, obj.size());
        } while (end <= obj.size() && end != begin);

        log.info("AllEvents:{},Batches:{}", events.size(), count);

    }

    private void addInBuilderQueue(InventoryDataBuilderQueueObject inventoryDataBuilderQueueObject) {

        streamBridge.send("reconTrigger-out-0", MessageBuilder.withPayload(inventoryDataBuilderQueueObject)
                .setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
                .setHeader("type", inventoryDataBuilderQueueObject.getClass().toString())
                .build());
    }
}
