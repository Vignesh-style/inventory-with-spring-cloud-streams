package in.co.sa.inventory.commons;

public class InventoryEventInput {

    private static final long serialVersionUID = 1L;
    private final NameIDType nameIdType;
    private final Object event;

    public InventoryEventInput(NameIDType nameIdType, Object event)
    {
        this.nameIdType = nameIdType;
        this.event = event;
    }

    public NameIDType getNameIdType() {
        return nameIdType;
    }

    public Object getEvent() {
        return event;
    }

    @Override
    public String toString() {
        return "InventoryEventInput{" +
                "nameIdType=" + nameIdType +
                ", event=" + event +
                '}';
    }

}
