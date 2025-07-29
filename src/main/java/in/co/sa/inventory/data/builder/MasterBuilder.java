package in.co.sa.inventory.data.builder;


import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.utils.Utils;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;


@Service
public class MasterBuilder {

    @Autowired
    ApplicationContext applicationContext;

    private final StreamBridge streamBridge;
    private static final Logger log = LogManager.getLogger(MasterBuilder.class);
    private static final ExecutorService dataBuilderPool = Executors.newFixedThreadPool(10);

    public MasterBuilder(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PreDestroy
    public void shutdown() {
        dataBuilderPool.shutdown();
        System.out.println("MasterBuilderService dataBuilderPool shuttingDown: ");
    }

    @Bean
    public Consumer<QueueObject> builderIn0() {
        return msg -> {

            System.out.println("Builder processing: " + msg);
//            msg.put("builderTime", System.currentTimeMillis());


            InventoryDataBuilderQueueObject streamPayload = (InventoryDataBuilderQueueObject) msg;
            String builderBeanName = streamPayload.builderInformation.getBuilderBeanName();

            InventoryOperationType operationType = streamPayload.getOperationType();

            if(streamPayload.isPostReconCommand())
            {
                addInPersisterQueue(streamPayload);
                return;
            }

            Map<NameIDType, Object> nitToEventMap = streamPayload.getNitToMOEventMap();
            Set<NameIDType> nitSet = nitToEventMap.keySet();

            if(Utils.isCollectionNullOrEmpty(nitSet))
                return;

            List<InventoryDataBuilderJob> inventoryDataBuilderJobs = getInventoryDataBuilderJobs(streamPayload.getBuilderQueueInput(), operationType, streamPayload.isReconEvent(), streamPayload.builderInformation, streamBridge);

            long startTime = System.currentTimeMillis();
            List<InventoryDataBuilderResult> executeJobs = executeJobs(inventoryDataBuilderJobs);
            long endTime = System.currentTimeMillis();

            log.info("Time taken by builder isReconEvent :{}, ObjectType :{}, OperationType:{}, Count:{}, BuilderJobsCount:{}, ExecuteJobsCount:{} is {}ms", streamPayload.isReconEvent(), new ArrayList<>(nitSet).get(0).getType(), operationType, nitSet.size(), inventoryDataBuilderJobs.size(), executeJobs.size(), (endTime - startTime));

            addInPersisterQueue(operationType, streamPayload.getBuilderQueueInput().getEvents(), executeJobs, streamPayload.isReconEvent(), streamPayload.builderInformation);

        };
    }

    private List<InventoryDataBuilderJob> getInventoryDataBuilderJobs(BuilderQueueInput builderQueueInput, InventoryOperationType operationType, boolean reconEvent, InventoryDataBuilderConfig builderInformation, StreamBridge streamBridge)
    {
        OperationTypeAndStatus status = operationType == InventoryOperationType.ADD ? new OperationTypeAndStatus(InventoryOperationType.ADD, RCAInventoryStatus.PENDING) :
                operationType == InventoryOperationType.UPDATE ? new OperationTypeAndStatus(InventoryOperationType.UPDATE, RCAInventoryStatus.PENDING) :
                        new OperationTypeAndStatus(InventoryOperationType.DELETE, RCAInventoryStatus.PENDING);

        List<InventoryDataBuilderJob> jobs = new ArrayList<>();
        Map<NameIDType, InventoryEventInput> nitToInventoryEventInputMap = builderQueueInput.getNitToInventoryEventInputMap();
        List<InventoryDbPersistData> dbDataList = new ArrayList<>();

        InventoryDataBuilder builder = applicationContext.getBean(builderInformation.getBuilderBeanName(), InventoryDataBuilder.class);

        for (Map.Entry<NameIDType, InventoryEventInput> entry : nitToInventoryEventInputMap.entrySet())
        {
            NameIDType nameIDType = entry.getKey();
            InventoryEventInput value = entry.getValue();
            Object event = value.getEvent();

            if(!reconEvent)
            {
                boolean validInput = builder.isValidInventoryEvent(nameIDType, value.getEvent());
                if(!validInput)
                {
                    log.debug("Invalid input for builder:{}, NIT:{}, OperType:{}", builderInformation.getName(), nameIDType, operationType);
                    continue;
                }
            }

            InventoryDbPersistData dbPersistData = new InventoryDbPersistData(new InventoryEventInput(nameIDType, event), status, null);
            dbDataList.add(dbPersistData);

            InventoryDataBuilderJob additionInventoryDataBuilderJob = new InventoryDataBuilderJob(operationType, nameIDType, event, reconEvent && operationType == InventoryOperationType.UPDATE ? ((ReconUpdateInventoryEventInput)value).getPersistedUserObject() : null , builder, builderInformation, reconEvent, streamBridge);
            jobs.add(additionInventoryDataBuilderJob);
        }

        InventoryDataPersisterQueueObject persisterQueueObject = new InventoryDataPersisterQueueObject(dbDataList, builderInformation);

        //dataPersisterMaster.getInventoryDataPersister().storeStatus(dbDataList);
        //sleepForAWhile(10000);

        this.streamBridge.send("builder-out-0", persisterQueueObject);

        return jobs;
    }


    private<T> List<T> executeJobs(List<? extends Callable<T>> jobs)
    {
        List<T> list = new ArrayList<T>();
        try
        {
            List<Future<T>> futures = dataBuilderPool.invokeAll(jobs);
            for(Future<T> future : futures)
            {
                try
                {
                    T t = future.get();
                    if(t != null)
                        list.add(t);
                }
                catch (ExecutionException e)
                {
                    log.catching(e);
                }
            }
        }
        catch (InterruptedException e)
        {
            log.catching(e);
        }
        return list;
    }


    public void addInPersisterQueue(QueueObject downStream)
    {
        streamBridge.send("builder-out-0", downStream);
    }

    private void addInPersisterQueue(InventoryOperationType operationType, List<InventoryEventInput> events, List<InventoryDataBuilderResult> jobsResult, boolean isReconEvent, InventoryDataBuilderConfig builderInformation)
    {
        if(Utils.isCollectionNullOrEmpty(jobsResult))
            return;

        Iterator<InventoryDataBuilderResult> iterator = jobsResult.iterator();
        while(iterator.hasNext())
        {
            InventoryDataBuilderResult next = iterator.next();
            if(!next.isSuccess())
            {
                iterator.remove();
            }
        }

        if(jobsResult.isEmpty())
            return;

        addInPersisterQueue(new InventoryDataPersisterQueueObject(operationType, events, jobsResult, isReconEvent, builderInformation));
    }
}
