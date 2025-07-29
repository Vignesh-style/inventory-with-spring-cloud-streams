package in.co.sa.inventory.commons;


import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;

import java.util.Map;

public class InventoryDataBuilderQueueObject extends QueueObject {

    private static final long serialVersionUID = 1L;
    final BuilderQueueInput builderQueueInput;
    public InventoryDataBuilderConfig builderInformation;

    public InventoryDataBuilderQueueObject(BuilderQueueInput builderQueueInput, boolean isReconEvent, InventoryDataBuilderConfig builderInfo)
    {
        super(builderQueueInput.getOperationType(), isReconEvent);
        this.builderQueueInput = builderQueueInput;
        this.builderInformation = builderInfo;
    }

    public InventoryDataBuilderConfig getBuilderInformation() {
        return builderInformation;
    }

    public void setBuilderInformation(InventoryDataBuilderConfig builderInformation) {
        this.builderInformation = builderInformation;
    }

    public BuilderQueueInput getBuilderQueueInput() {
        return builderQueueInput;
    }

    public Map<NameIDType, Object> getNitToMOEventMap() {
        return builderQueueInput.getNitToEventMap();
    }
}
