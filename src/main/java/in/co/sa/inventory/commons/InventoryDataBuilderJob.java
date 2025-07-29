package in.co.sa.inventory.commons;

import in.co.nmsworks.sa.inv.builder.InventoryDataBuilder;
import in.co.nmsworks.sa.inv.config.entities.InventoryDataBuilderConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

public class InventoryDataBuilderJob implements Callable<InventoryDataBuilderResult>
{
    private static final Logger logger = LogManager.getLogger(InventoryDataBuilderJob.class);

    final InventoryOperationType inventoryOperationType;
    final InventoryDataBuilderConfig builderConfig;
    final InventoryDataBuilder dataBuilder;
    final NameIDType nit;
    final Object discoveredEvent;
    final Object persistedObject;
    final boolean isReconEvent;
    StreamBridge streamBridge;


    public InventoryDataBuilderJob(InventoryOperationType inventoryOperationType, NameIDType invBuilderDetail, Object discoveredEvent, Object persistedObject, InventoryDataBuilder dataBuilder, InventoryDataBuilderConfig builderConfig, boolean isReconEvent, StreamBridge streamBridge)
    {
        this.inventoryOperationType = inventoryOperationType;
        this.nit = invBuilderDetail;
        this.discoveredEvent = discoveredEvent;
        this.persistedObject = persistedObject;
        this.dataBuilder = dataBuilder;
        this.isReconEvent = isReconEvent;
        this.builderConfig = builderConfig;
        this.streamBridge = streamBridge;
    }

    @Override
    public InventoryDataBuilderResult call()
    {
        InventoryDataBuilderResult buildData = null;
        long start = System.currentTimeMillis();

        try
        {
            switch(inventoryOperationType)
            {
                case ADD :
                    buildData = dataBuilder.getInventoryDataForAdd(nit, discoveredEvent, isReconEvent);
                    break;
                case UPDATE :
                    buildData = dataBuilder.getInventoryDataforUpdate(nit, discoveredEvent, persistedObject, isReconEvent);
                    break;
                case DELETE :
                    buildData = dataBuilder.getInventoryDataforDelete(nit, discoveredEvent, isReconEvent);
                    break;
                default:
                    break;
            }

            List<InventoryDbPersistData> inventoryDbPersistData = new ArrayList<>();

            if(buildData == null)
            {
                logger.info("Received BuildData is null..Updating builder failed status:{}, Op:{}", nit, inventoryOperationType);
                inventoryDbPersistData = Collections.singletonList(new InventoryDbPersistData(new InventoryEventInput(nit, discoveredEvent), new OperationTypeAndStatus(inventoryOperationType, RCAInventoryStatus.BUILDER_FAILED), "Builder failed with build data null"));
            }

            if(buildData != null && !buildData.isSuccess() && buildData.getTypeAndStatus().getStatus() == RCAInventoryStatus.BUILDER_FAILED)
            {
                logger.info("Received BuildData is not-null..Updating builder failed status:{}, Op:{}", nit, inventoryOperationType);
                inventoryDbPersistData = Collections.singletonList(new InventoryDbPersistData(new InventoryEventInput(nit, discoveredEvent), buildData.getTypeAndStatus(), buildData.getErrorReason()));
            }

            InventoryDataPersisterQueueObject builderDownStream = new InventoryDataPersisterQueueObject(inventoryDbPersistData, builderConfig);
            streamBridge.send("builder-out-0", builderDownStream);

        }
        catch (Throwable e)
        {
            logger.error("Exception caught");
            logger.error("Exception occurred while forming Inventory DataBuilder Job...- {}", nit, e);

            String errorReason = "BUILDER JOB FAILED" + e.getMessage();
            buildData = new InventoryDataBuilderResult(nit, new OperationTypeAndStatus(inventoryOperationType, RCAInventoryStatus.BUILDER_FAILED), discoveredEvent, false, errorReason);
        }

        long end = System.currentTimeMillis();
        logger.debug("Time taken to build addition inventory data for {} = {} ms", nit.getName(), end - start);
        return buildData;
    }
}