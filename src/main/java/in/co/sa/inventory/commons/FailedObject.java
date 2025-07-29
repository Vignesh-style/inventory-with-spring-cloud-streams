package in.co.sa.inventory.commons;

public class FailedObject {

    private static final long serialVersionUID = 1L;

    private final NameIDType nit;
    private final OperationTypeAndStatus typeAndStatus;
    private final String errorReason;

    public FailedObject(NameIDType nit, OperationTypeAndStatus typeAndStatus, String errorReason)
    {
        super();
        this.nit = nit;
        this.typeAndStatus = typeAndStatus;
        this.errorReason = errorReason;
    }

    public NameIDType getNit()
    {
        return nit;
    }

    public String getErrorReason()
    {
        return errorReason;
    }

    public OperationTypeAndStatus getTypeAndStatus()
    {
        return typeAndStatus;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nit == null) ? 0 : nit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FailedObject other = (FailedObject) obj;
        if (nit == null)
        {
            if (other.nit != null)
                return false;
        }
        else if (!nit.equals(other.nit))
            return false;
        return true;
    }
}