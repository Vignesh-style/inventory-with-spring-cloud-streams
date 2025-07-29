package in.co.sa.inventory.commons;

import java.io.Serializable;

public class InventoryDataBuilderResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final NameIDType nameIDType;
    private final Object inventoryData;
    private final boolean isSuccess;
    private final String errorReason;
    private final OperationTypeAndStatus typeAndStatus;

    public InventoryDataBuilderResult(NameIDType nameIDType, OperationTypeAndStatus typeAndStatus, Object inventoryData, boolean isSuccess, String errorReason)
    {
        this.nameIDType = nameIDType;
        this.typeAndStatus = typeAndStatus;
        this.inventoryData = inventoryData;
        this.isSuccess = isSuccess;
        this.errorReason = errorReason;
    }

    public OperationTypeAndStatus getTypeAndStatus()
    {
        return typeAndStatus;
    }

    public Object getInventoryData() {
        return inventoryData;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public NameIDType getNameIDType()
    {
        return nameIDType;
    }
}