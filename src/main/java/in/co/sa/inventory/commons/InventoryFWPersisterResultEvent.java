package in.co.sa.inventory.commons;

public class InventoryFWPersisterResultEvent extends GenericEvent
{
    private final NameIDType nameIDType;
    private final InventoryOperationType operationType;
    private final Object objectToNotify;

    public InventoryFWPersisterResultEvent(NameIDType nameIDType, InventoryOperationType operationType, Object objectToNotify)
    {
        this.nameIDType = nameIDType;
        this.operationType = operationType;
        this.objectToNotify = objectToNotify;
    }

    public NameIDType getNameIDType() {
        return nameIDType;
    }

    public InventoryOperationType getOperationType() {
        return operationType;
    }

    public Object getObjectToNotify() {
        return objectToNotify;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("InventoryFWPersisterResultEvent [nameIDType=");
        builder.append(nameIDType);
        builder.append(", operationType=");
        builder.append(operationType);
        builder.append(", objectToNotify=");
        builder.append(objectToNotify == null ? null : objectToNotify.getClass().getSimpleName());
        builder.append("]");
        return builder.toString();
    }
}