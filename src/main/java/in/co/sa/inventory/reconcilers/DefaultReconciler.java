package in.co.sa.inventory.reconcilers;


import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public abstract class DefaultReconciler implements Reconciler {

    public final Logger logger = LogManager.getLogger(getClass());

    public abstract DiscoveredMOInput getDiscoveredMOInput();
    public abstract PersistedMOInput getPersistedMOInput();

    @Override
    public ReconResult doRecon(ReconInput reconInput)
    {
        ReconResult reconResult = new ReconResult();

        DiscoveredMOInput discoveredMOInput = getDiscoveredMOInput();
        PersistedMOInput persistedMOInput = getPersistedMOInput();

        Map<String, Map<NameIDType, Object>> discoveredManagedObjects = discoveredMOInput == null ? new HashMap<String, Map<NameIDType, Object>>() : discoveredMOInput.getDiscoveredMODataMap();
        Map<String, Map<NameIDType, PersistedMOData>> persistedManagedObjects = persistedMOInput == null ? new HashMap<String, Map<NameIDType, PersistedMOData>>() : persistedMOInput.getPersistedMODataMap();

        Set<String> objectTypes = new HashSet<>();
        objectTypes.addAll(discoveredManagedObjects.keySet());
        objectTypes.addAll(persistedManagedObjects.keySet());

        for (String objectType : objectTypes)
        {
            ReconData reconDataObj = new ReconData();

            Map<NameIDType, Object> discoverdMOListForObjectType = discoveredManagedObjects.get(objectType);
            Map<NameIDType, PersistedMOData> persistedMOListForObjectType = persistedManagedObjects.get(objectType);

            if(Utils.isMapNullOrEmpty(discoverdMOListForObjectType))
            {
                if(Utils.isMapNullOrEmpty(persistedMOListForObjectType))
                {
                    logger.info("Persisted and discovered both empty...{}", objectType);
                    continue;
                }

                logger.info("Adding all persistedMOListForObjectType list {} to DELETE", objectType);
                reconDataObj.addToDelList(getPersistedInventoryEvents(persistedMOListForObjectType));
                reconResult.addToReconDataMap(objectType, reconDataObj);
                continue;
            }

            else if(Utils.isMapNullOrEmpty(persistedMOListForObjectType))
            {
                logger.info("Adding all discoverdMOListForObjectType list {} to ADD", objectType);
                reconDataObj.addToAddList(getDiscoveredInventoryEvents(discoverdMOListForObjectType));
                reconResult.addToReconDataMap(objectType, reconDataObj);
                continue;
            }

            logger.info("Both list are not empty....{}", objectType);
            addReconData(objectType, discoverdMOListForObjectType, persistedMOListForObjectType, reconInput, reconDataObj);

            reconResult.addToReconDataMap(objectType, reconDataObj);
        }
        return reconResult;
    }

    public void addReconData(String objectType,Map<NameIDType, Object> discoverdMOListForObjectType, Map<NameIDType, PersistedMOData> persistedMOListForObjectType,
                             ReconInput reconInput, ReconData reconDataObj)
    {
        Set<NameIDType> toAdd = new HashSet<>();
        Set<NameIDType> toUpdate = new HashSet<>();
        Set<NameIDType> toDelete = new HashSet<>();

        Utils.diff(discoverdMOListForObjectType.keySet(), persistedMOListForObjectType.keySet(), toAdd, toDelete, toUpdate);

        List<InventoryEventInput> toAddList = getInventoryEvents(toAdd, discoverdMOListForObjectType);
        List<ReconUpdateInventoryEventInput> toUpdateList = new ArrayList<>();
        List<InventoryEventInput> toDeleteList = getInventoryEvents(toDelete, getPersistedNitToUserObject(persistedMOListForObjectType));

        for (NameIDType nameIDType : toUpdate)
        {
            PersistedMOData persistedMOData = persistedMOListForObjectType.get(nameIDType);
            logger.trace("Nit:{},PersistedMOData:{}", nameIDType, persistedMOData);

            OperationTypeAndStatus typeAndStatus = persistedMOData.getOperationTypeAndStatus();

            RCAInventoryStatus status = typeAndStatus.getStatus();
            if(!isValidStatus(status, reconInput.isRetriggerfailedObjects()))
                continue;

            switch (typeAndStatus.getOperationType())
            {
                case ADD:
                    toAddList.add(getInventoryEvent(nameIDType, discoverdMOListForObjectType.get(nameIDType)));
                    break;
                case UPDATE:
                    toUpdateList.add(getReconUpdateInventoryEvent(nameIDType, discoverdMOListForObjectType.get(nameIDType), persistedMOData.getUserObject()));
                    break;
                case DELETE:
                    toDeleteList.add(getInventoryEvent(nameIDType, persistedMOData.getUserObject()));
                    break;
                default :
                    break;
            }
        }

        reconDataObj.addToAddList(toAddList);
        reconDataObj.addToUpdateList(toUpdateList);
        reconDataObj.addToDelList(toDeleteList);

    }
    private Map<NameIDType, Object> getPersistedNitToUserObject(Map<NameIDType, PersistedMOData> persistedMOListForObjectType)
    {
        Map<NameIDType, Object> map = new HashMap<>();
        for(Map.Entry<NameIDType, PersistedMOData> entry : persistedMOListForObjectType.entrySet())
        {
            PersistedMOData persistedMOData = entry.getValue();
            map.put(persistedMOData.getNameIDType(), persistedMOData.getUserObject());
        }
        return map;
    }

//    @Override
//    public void reconEventsSubmitted(String objectType, InventoryDataBuilderMaster invDBMasterobj) {
//
//    }
//
//    @Override
//    public void persistenceCompleted(String objectType, InventoryDataBuilderMaster invDBMasterobj) {
//
//    }

    private boolean isValidStatus(RCAInventoryStatus status, boolean isRetriggerfailedObjects)
    {
        if(status == RCAInventoryStatus.PENDING)
            return true;

        if( (status == RCAInventoryStatus.BUILDER_FAILED || status ==  RCAInventoryStatus.PERSISTER_FAILED) && isRetriggerfailedObjects)
            return true;

        return false;
    }

    protected ReconUpdateInventoryEventInput getReconUpdateInventoryEvent(NameIDType nameIDType, Object object, Object userObject)
    {
        return new ReconUpdateInventoryEventInput(nameIDType, object, userObject);
    }

    private List<InventoryEventInput> getInventoryEvents(Set<NameIDType> nitList, Map<NameIDType, Object> nitToUserObjMap)
    {
        List<InventoryEventInput> result = new ArrayList<>();
        if(Utils.isCollectionNullOrEmpty(nitList))
            return result;

        for (NameIDType nameIDType : nitList)
            result.add(getInventoryEvent(nameIDType, nitToUserObjMap.get(nameIDType)));

        return result;
    }

    private InventoryEventInput getInventoryEvent(NameIDType nameIDType, Object userObject) {
        return new InventoryEventInput(nameIDType, userObject);
    }

    public List<InventoryEventInput> getDiscoveredInventoryEvents(Map<NameIDType, Object> discoverdMOListForObjectType)
    {
        List<InventoryEventInput> events = new ArrayList<>();
        for (Map.Entry<NameIDType, Object> entry : discoverdMOListForObjectType.entrySet())
            events.add(getInventoryEvent(entry.getKey(), entry.getValue()));

        return events;
    }

    public List<InventoryEventInput> getPersistedInventoryEvents(Map<NameIDType, PersistedMOData> persistedMOListForObjectType) {

        List<InventoryEventInput> events = new ArrayList<>();
        for (Map.Entry<NameIDType, PersistedMOData> entry : persistedMOListForObjectType.entrySet())
            events.add(getInventoryEvent(entry.getKey(), entry.getValue().getUserObject()));

        return events;
    }

}
