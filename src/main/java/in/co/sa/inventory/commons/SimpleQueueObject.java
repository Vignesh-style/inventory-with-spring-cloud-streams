package in.co.sa.inventory.commons;

public class SimpleQueueObject extends QueueObject{

    private static final long serialVersionUID = 2L;

    public SimpleQueueObject(InventoryOperationType operationType, boolean isReconEvent) {
        super(operationType, isReconEvent);
    }
}
