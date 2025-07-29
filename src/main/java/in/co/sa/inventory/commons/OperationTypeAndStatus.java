package in.co.sa.inventory.commons;


public class OperationTypeAndStatus {

    private final InventoryOperationType operationType;
    private final RCAInventoryStatus status;
    private Object additionalInfo;
    private String key;

    public OperationTypeAndStatus(InventoryOperationType operationType, RCAInventoryStatus status)
    {
        this.operationType = operationType;
        this.status = status;
        this.key = operationType.name() + '$' + status.name();
    }

    public OperationTypeAndStatus(InventoryOperationType operationType, RCAInventoryStatus status, Object additionalInfo)
    {
        this.operationType = operationType;
        this.status = status;
        this.additionalInfo = additionalInfo;
        this.key = operationType.name() + '$' + status.name();
    }

    public InventoryOperationType getOperationType()
    {
        return operationType;
    }

    public RCAInventoryStatus getStatus()
    {
        return status;
    }

    public Object getAdditionalInfo()
    {
        return additionalInfo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static boolean isCurrentAndPreviousStateEqual(OperationTypeAndStatus current, OperationTypeAndStatus previous)
    {
        return current.getOperationType() == previous.getOperationType() && current.getStatus() == previous.getStatus();
    }

    public static boolean isDeleteCompletedState(OperationTypeAndStatus currentState)
    {
        return currentState.getOperationType() == InventoryOperationType.DELETE && currentState.getStatus() == RCAInventoryStatus.COMPLETED;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("OperationTypeAndStatus [operationType=");
        builder.append(operationType);
        builder.append(", status=");
        builder.append(status);
        builder.append("]");
        return builder.toString();
    }

}
