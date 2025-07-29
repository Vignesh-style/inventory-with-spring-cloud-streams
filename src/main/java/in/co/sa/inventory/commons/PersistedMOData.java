package in.co.sa.inventory.commons;

public class PersistedMOData {

    private final NameIDType nameIDType;
    private final OperationTypeAndStatus operationTypeAndStatus;
    private final Object userObject;

    public PersistedMOData(NameIDType nameIDType, OperationTypeAndStatus operationTypeAndStatus, Object userObject)
    {
        this.nameIDType = nameIDType;
        this.operationTypeAndStatus = operationTypeAndStatus;
        this.userObject = userObject;
    }

    public NameIDType getNameIDType()
    {
        return nameIDType;
    }

    public OperationTypeAndStatus getOperationTypeAndStatus()
    {
        return operationTypeAndStatus;
    }

    public Object getUserObject()
    {
        return userObject;
    }

}
