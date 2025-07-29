package in.co.sa.inventory.commons;

public class ReconUpdateInventoryEventInput extends InventoryEventInput {

    private final Object persistedUserObject;

    public ReconUpdateInventoryEventInput(NameIDType nameIdType, Object event, Object persistedUserObject)
    {
        super(nameIdType, event);
        this.persistedUserObject = persistedUserObject;
    }

    public Object getPersistedUserObject()
    {
        return persistedUserObject;
    }
}
