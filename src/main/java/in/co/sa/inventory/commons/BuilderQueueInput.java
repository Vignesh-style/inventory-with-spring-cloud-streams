package in.co.sa.inventory.commons;

import in.co.nmsworks.sa.inv.utils.InventoryBuilderUtils;
import in.co.nmsworks.sa.inv.utils.Utils;

import java.io.Serializable;
import java.util.*;

public class BuilderQueueInput implements Serializable {

    private static final long serialVersionUID = 1L;
    private final InventoryOperationType operationType;
    private List<InventoryEventInput> events;
    private int batchSize;

    public BuilderQueueInput(InventoryOperationType operationType, int batchSize) {
        this.operationType = operationType;
        this.batchSize = batchSize;
    }

    public BuilderQueueInput(InventoryOperationType operationType, List<InventoryEventInput> events, int batchSize) {
        this.operationType = operationType;
        this.events = events;
        this.batchSize = batchSize;
    }


    public void addEvent(InventoryEventInput eventInput)
    {
        if(Utils.isCollectionNullOrEmpty(events))
            events = new ArrayList<>();

        events.add(eventInput);
    }

    public InventoryOperationType getOperationType() {
        return operationType;
    }

    public List<InventoryEventInput> getEvents() {
        return events;
    }

    public Map<NameIDType, Object> getNitToEventMap()
    {
        return InventoryBuilderUtils.getNitToEventMap(events);
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public String getObjectType()
    {
        if(Utils.isCollectionNotNullAndNotEmpty(events))
            return events.get(0).getNameIdType().getType();

        return null;
    }

    public Map<NameIDType, InventoryEventInput> getNitToInventoryEventInputMap()
    {
        if(Utils.isCollectionNullOrEmpty(events))
            return Collections.emptyMap();

        Map<NameIDType, InventoryEventInput> result = new HashMap<NameIDType, InventoryEventInput>();
        for (InventoryEventInput inventoryEventInput : events)
            result.put(inventoryEventInput.getNameIdType(), inventoryEventInput);

        return result;
    }
}