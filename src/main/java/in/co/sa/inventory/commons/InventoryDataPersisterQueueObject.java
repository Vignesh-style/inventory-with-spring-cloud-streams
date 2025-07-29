package in.co.sa.inventory.commons;

import in.co.nmsworks.sa.inv.config.entities.InventoryDataBuilderConfig;
import in.co.nmsworks.sa.inv.utils.InventoryBuilderUtils;
import in.co.nmsworks.sa.inv.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDataPersisterQueueObject extends QueueObject
{
    private static final long serialVersionUID = -7113369073442926348L;
    final List<InventoryEventInput> inventoryEvents;
    final List<InventoryDataBuilderResult> dataToPersist;
    final Map<NameIDType, Object> nitToAdditionalInfoMap = new HashMap<>();
    final List<InventoryDbPersistData> persistDataList;
    public final boolean isBuilderPreProcessingEventsNotification;


    //todo we can have another queue object for this case handling ...

    public InventoryDataPersisterQueueObject(InventoryOperationType operationType, List<InventoryEventInput> inventoryEvents, List<InventoryDataBuilderResult> inventoryData, boolean isReconEvent, InventoryDataBuilderConfig builderInfo)
    {
        super(operationType, isReconEvent);
        this.dataToPersist = inventoryData;
        this.inventoryEvents = inventoryEvents;
        this.persistDataList = new ArrayList<>();
        this.isBuilderPreProcessingEventsNotification = false;
        this.builderInformation = builderInfo;
        fillNitToAdditionalInfoMap();
    }

    public InventoryDataPersisterQueueObject(List<InventoryDbPersistData> persistDataList, InventoryDataBuilderConfig builderInfo)
    {
        super(null, false);
        this.isBuilderPreProcessingEventsNotification = true;
        this.persistDataList = persistDataList;
        this.dataToPersist = null;
        this.inventoryEvents = new ArrayList<>();
        this.builderInformation = builderInfo;
    }

    private void fillNitToAdditionalInfoMap()
    {
        if(Utils.isCollectionNullOrEmpty(dataToPersist))
            return;

        for (InventoryDataBuilderResult inventoryDataBuilderResult : dataToPersist)
        {
            nitToAdditionalInfoMap.put(inventoryDataBuilderResult.getNameIDType(), inventoryDataBuilderResult.getTypeAndStatus().getAdditionalInfo());
        }
    }

    public List<InventoryDataBuilderResult> getDataToPersist()
    {
        return dataToPersist;
    }

    public List<InventoryEventInput> getInventoryEvents() {
        return inventoryEvents;
    }

    public Map<NameIDType, Object> getNitToEventMap()
    {
        return InventoryBuilderUtils.getNitToEventMap(inventoryEvents);
    }

    public Map<NameIDType, Object> getNitToAdditionalInfoMap()
    {
        return nitToAdditionalInfoMap;
    }

    public List<InventoryDbPersistData> getPersistDataList() {
        return persistDataList;
    }
}