package in.co.sa.inventory.persister;


import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class MasterPersister {

    @Autowired
    ApplicationContext context;
    
    private static final Logger log = LogManager.getLogger(MasterPersister.class);

    @Bean
    public Consumer<QueueObject> persisterIn0() {

        return msg -> {

            System.out.println("Persisting final message: " + msg);

            if(msg.isPostReconCommand())
            {
                PostReconInvokerCommand command = (PostReconInvokerCommand) msg;
                command.invokePostRecon();
                return;
            }

            InventoryDataPersisterQueueObject payLoadStream = (InventoryDataPersisterQueueObject) msg;

            if (payLoadStream.isBuilderPreProcessingEventsNotification) {
                flush(payLoadStream.getPersistDataList(), payLoadStream.builderInformation);
                return;
            }


            List<InventoryDataBuilderResult> dataToPersist = payLoadStream.getDataToPersist();
            
            if(Utils.isCollectionNullOrEmpty(dataToPersist))
               return;

            InventoryDataPersisterResult result = null;
            String persisterBeanName = payLoadStream.builderInformation.getPersisterBeanName();
            InventoryDataPersister persister = context.getBean(persisterBeanName, InventoryDataPersister.class);
            

            long startTime = System.currentTimeMillis();
            switch (payLoadStream.getOperationType())
            {
                case ADD :
                    result = persister.doWorkForAdd(dataToPersist);
                    break;
                case UPDATE :
                    result = persister.doWorkForUpdate(dataToPersist);
                    break;
                case DELETE :
                    result = persister.doWorkForDelete(dataToPersist);
                    break;
                default:
                    break;
            }
            long checkPointTime = System.currentTimeMillis();
            log.info("inventoryDataPersister.doWorkFor for Persisted data completed in  : {} & size : {}", checkPointTime - startTime, payLoadStream.getDataToPersist().size());
            storeStatus(result, payLoadStream.getOperationType(), payLoadStream.getNitToEventMap(), payLoadStream.getNitToAdditionalInfoMap(), payLoadStream.builderInformation);
            log.info("inventoryDataPersister.storeStatus completion for Persisted data in  : {}", System.currentTimeMillis() - checkPointTime);

            List<InventoryFWPersisterResultEvent> eventsToNotify = result.getEventsToNotify();
            log.trace("Events to notify : {}", eventsToNotify);
            String channelName = payLoadStream.builderInformation.getChannelName();

            log.trace("channel Name : {}", channelName);
            if(Utils.isStringNullOrEmpty(channelName, true) || Utils.isCollectionNullOrEmpty(eventsToNotify))
                return;

            long endTime = System.currentTimeMillis();
            log.info("Time taken to persist ObjectType :{}, OperationType:{}, Count:{}, :{}ms", dataToPersist.get(0).getNameIDType().getType(), payLoadStream.getOperationType(), dataToPersist.size(), (endTime - startTime));

            long startNotify = System.currentTimeMillis();
            log.info("Notifying events....{}", eventsToNotify.size());

            //todo we have notify these thing to the result channel for adapter consuming...

//            NotifEngineFactory.addEventToQueue(channelName, eventsToNotify);
            long endNotify = System.currentTimeMillis();
            log.info("Time taken to notify {} events : {} ", eventsToNotify.size(), (endNotify - startNotify));


        };
    }

    private void flush(List<InventoryDbPersistData> persistDataList, InventoryDataBuilderConfig builderInformation) {
        InventoryDataPersister persister = context.getBean(builderInformation.getPersisterBeanName(), InventoryDataPersister.class);
        persister.updateState(persistDataList);
    }

    private void storeStatus(InventoryDataPersisterResult result, InventoryOperationType inventoryOperationType, Map<NameIDType, Object> map, Map<NameIDType, Object> nitToAdditionalInfoMap, InventoryDataBuilderConfig builderInformation)
    {

        log.info("InventoryDataPersisterResult : result :: : {} : {},  : {} : {}", "completed", result.getCompletedObjects().size(), "failed", result.getFailedObjects().size());

        List<InventoryDbPersistData> dataList = new ArrayList<>();

        List<NameIDType> completedObjects = result.getCompletedObjects();
        if(Utils.isCollectionNotNullAndNotEmpty(completedObjects))
        {
            for (NameIDType nameIDType : completedObjects)
            {
                OperationTypeAndStatus rcaInventoryStatus = inventoryOperationType == InventoryOperationType.POST_RECON_INVOKER_COMMAND ? null : new OperationTypeAndStatus(inventoryOperationType, RCAInventoryStatus.COMPLETED, nitToAdditionalInfoMap.get(nameIDType));
//                inventoryDataPersister.storeStatus(Collections.singletonList(getInventoryEventInputObject(nameIDType, map)), rcaInventoryStatus, null);

                InventoryDbPersistData dbPersistData = new InventoryDbPersistData(getInventoryEventInputObject(nameIDType, map), rcaInventoryStatus, null);
                dataList.add(dbPersistData);
            }
        }

        List<FailedObject> failedObjects = result.getFailedObjects();
        if(Utils.isCollectionNotNullAndNotEmpty(failedObjects))
        {
            for (FailedObject failedObject : failedObjects) {

                NameIDType nit = failedObject.getNit();
                OperationTypeAndStatus status = failedObject.getTypeAndStatus();
                String errorReason = failedObject.getErrorReason();

                InventoryDbPersistData data = new InventoryDbPersistData(getInventoryEventInputObject(nit, map), status, errorReason);
                dataList.add(data);

//                inventoryDataPersister.storeStatus(Collections.singletonList(getInventoryEventInputObject(nit, map)), status, errorReason);
            }
        }
        log.info("dataList : {}", dataList.size());
        flush(dataList, builderInformation);
    }

    private InventoryEventInput getInventoryEventInputObject(NameIDType nameIDType, Map<NameIDType, Object> nitToEventMap) {
        return new InventoryEventInput(nameIDType, nitToEventMap.get(nameIDType));
    }
}
