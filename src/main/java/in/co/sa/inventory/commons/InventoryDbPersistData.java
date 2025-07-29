package in.co.sa.inventory.commons;

public class InventoryDbPersistData {

    private InventoryEventInput eventInput;
    private OperationTypeAndStatus operationTypeAndStatus;
    private String errorReason;


    public InventoryDbPersistData(InventoryEventInput eventInput, OperationTypeAndStatus operationTypeAndStatus, String errorReason) {
        this.eventInput = eventInput;
        this.operationTypeAndStatus = operationTypeAndStatus;
        this.errorReason = errorReason;
    }


    public InventoryEventInput getEventInput() {
        return eventInput;
    }

    public void setEventInput(InventoryEventInput eventInput) {
        this.eventInput = eventInput;
    }

    public OperationTypeAndStatus getOperationTypeAndStatus() {
        return operationTypeAndStatus;
    }

    public void setOperationTypeAndStatus(OperationTypeAndStatus operationTypeAndStatus) {
        this.operationTypeAndStatus = operationTypeAndStatus;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }
}
