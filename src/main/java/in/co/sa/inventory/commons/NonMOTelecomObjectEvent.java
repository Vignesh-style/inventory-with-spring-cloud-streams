package in.co.sa.inventory.commons;

public class NonMOTelecomObjectEvent extends DAOEvent<Object> {

    private static final long serialVersionUID = -9206240602523554506L;

    public NonMOTelecomObjectEvent() {

    }

    public NonMOTelecomObjectEvent(Object object, DAOEventType eventType, String category) {
        setObject(object);
        setType(eventType);
        setCategory(category);
    }

}