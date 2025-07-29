package in.co.sa.inventory.online.listener;

import in.co.sa.inventory.commons.BuilderQueueInput;
import in.co.sa.inventory.commons.GenericEvent;
import in.co.sa.inventory.commons.InventoryDataBuilderQueueObject;
import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.w3c.dom.Element;

import java.util.List;

public abstract class InventoryFWEventsCollector {

//    protected final String builderName;
//    final EnrichedLinkedBlockingQueue<GenericEvent> eventsQueue;
//    EventsCollector eventsCollector = null;
//    EventsHandler eventsHandler = null;
//    CountDownLatch eventsCollectorLatch = null;

    protected final Logger logger = LogManager.getLogger(getClass());

    public abstract void registerListener(Element customInput);

    public abstract List<BuilderQueueInput> getBuilderQueueInputForBatchEvent(List<GenericEvent> events);

    public abstract void shutdownListener();

    public abstract boolean isBatchRequired();

    private boolean shutDown;

//    public InventoryFWEventsCollector(String builderName)
//    {
//        this.builderName = builderName;
//        eventsQueue = new EnrichedLinkedBlockingQueue<>(builderName + "-EventsQueue");
//    }

//    public void init()
//    {
//        if(!isBatchRequired())
//            return;
//
//        eventsCollector = new EventsCollector();
//        eventsHandler = new EventsHandler();
//        eventsCollector.start();
//    }

//    public void addEventToBatchQueue(GenericEvent event) throws RemoteException
//    {
//        while (true)
//        {
//            if (shutDown)
//                break;
//
//            if (eventsQueue.size() < 1000)
//            {
//                addInEventsQueue(event);
//                break;
//            }
//
//            sleepForAWhile();
//        }
////    }
//
//    private void sleepForAWhile()
//    {
//        try
//        {
//            Thread.sleep(100); //Sleeping for 100 milli seconds
//        }
//        catch (InterruptedException e)
//        {
//            logger.error("Exception while sleeping in InventoryFWEventsCollector ", e);
//        }
//    }
//
//    private void addInEventsQueue(GenericEvent event) throws RemoteException
//    {
//        logger.info("InventoryFWEventsCollector addInEventsQueue...eventsQueueSize==={}", eventsQueue.size());
//        try
//        {
//            eventsQueue.put(event);
//        }
//        catch (Exception e)
//        {
//            throw new RemoteException(e.getMessage(), e);
//        }
//    }
//
//    class EventsCollector extends BatchSendThread<GenericEvent>
//    {
//        public EventsCollector()
//        {
//            super(eventsQueue, ConfigurationFiles.getInt("objectsAccumulatorQueueTime", InventoryManager.RCA_INVENTORY), ConfigurationFiles.getInt("objectsAccumulatorFetchSize", InventoryManager.RCA_INVENTORY),
//                    new EventsHandler(), "EventsCollector", true);
//        }
//
//        @Override
//        public void run()
//        {
//            try
//            {
//                logger.info("EventsCollector run method called...builderName==={}", builderName);
//              /*  if(InventoryBuilderUtils.isReconEnabledForAnyReconciler(builderName))
//                {
//                    logger.info("Waiting for ObjectsReconciliator to complete its work before processing MO events....");
//
//                    eventsCollectorLatch.await();
//
//                    logger.info("Objects reconciliation completed. Now starting to process MO events.");
//                }*/
//            }
//            catch (Exception e)
//            {
//                logger.catching(e);
//            }
//
//            super.run();
//        }
//    }
//
//    class EventsHandler implements DataHandler<GenericEvent>
//    {
//        @Override
//        public void processData(List<GenericEvent> data) throws Exception
//        {
//            logger.info("EventsHandler processData eventsize==={}", CommonUtils.size(data));
//            try
//            {
//                if(!isBatchRequired())
//                    return;
//
//                List<BuilderQueueInput> builderQueueInputForBatchEvents = getBuilderQueueInputForBatchEvent(data);
//                logger.info("EventsHandler builderQueueInputForBatchEventssize==={}", CommonUtils.size(builderQueueInputForBatchEvents));
//
//                if(CommonUtils.isCollectionNullOrEmpty(builderQueueInputForBatchEvents))
//                    return;
//
//                InventoryDataBuilderMaster inventoryDataBuilderMaster = InventoryManager.getInventoryDataBuilderMaster(builderName);
//
//                Set<List<InventoryEventInput>> bQEvents = builderQueueInputForBatchEvents.stream().map(BuilderQueueInput::getEvents).collect(Collectors.toSet());
//
//                if (inventoryDataBuilderMaster.isReconUnderProgress){
//
//                    logger.info("FWEventC' Processing hold for events : {} :: due to recon happening in {} :: {}", bQEvents, builderName, inventoryDataBuilderMaster);
//
//                    eventsCollectorLatch = new CountDownLatch(1);
//                    eventsCollectorLatch.await();
//
//                    logger.info("FWEventC' Processing after::hold for events : {} :: after recon done {} :: {}", bQEvents, builderName, inventoryDataBuilderMaster);
//
//                }else {
//                    logger.info("FWEventC' Processing without::hold for events : {} :: while recon not happening {} :: {}", bQEvents, builderName, inventoryDataBuilderMaster);
//                }
//
//                for (BuilderQueueInput builderQueueInput : builderQueueInputForBatchEvents) {
//                    submitInput(builderQueueInput);
//                }
//            }
//            catch(Exception e)
//            {
//                logger.error("Error while processing events for builder :{}, Data:{}", builderName, data);
//                logger.error("", e);
//                throw e;
//            }
//        }
//    }
//
//    public void startProcessingMOEvents()
//    {
//        if(!isBatchRequired())
//            return;
//
//        eventsCollectorLatch.countDown();
//    }
//
//    public void shutDown()
//    {
//        this.shutDown = true;
//
//        if(!isBatchRequired())
//            return;
//
//        eventsCollectorLatch.countDown();
//        eventsCollector.shutDown();
//    }
//
//    public String getQueueName() {
//        return eventsQueue.getName();
//    }
//
//    public int getQueueSize() {
//        return eventsQueue.size();
//    }
//
//    public long getAddedEventsCount() {
//        return eventsQueue.getAddedCount();
//    }
//
//    public long getProcessedEventsCount() {
//        return eventsQueue.getProcessedCount();
//    }

    public void submitInput(BuilderQueueInput builderQueueInput, List<InventoryDataBuilderConfig> builderInformation, StreamBridge streamBridge)
    {
        for (InventoryDataBuilderConfig bI : builderInformation){

            InventoryDataBuilderQueueObject downStreamPayLoad = new InventoryDataBuilderQueueObject(builderQueueInput, false, bI);
            streamBridge.send("onlineSender-out-0", MessageBuilder.withPayload(downStreamPayLoad)
                    .setHeader(MessageHeaders.CONTENT_TYPE, "application/json")
                    .setHeader("type", downStreamPayLoad.getClass())
                    .build());

        }
    }
}