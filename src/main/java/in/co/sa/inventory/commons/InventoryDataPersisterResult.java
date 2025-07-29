package in.co.sa.inventory.commons;

import java.util.List;

public class InventoryDataPersisterResult {
    private final List<NameIDType> completedObjects;
    private List<FailedObject> failedObjects;
    private final List<InventoryFWPersisterResultEvent> eventsToNotify;

    public InventoryDataPersisterResult(List<NameIDType> completedObjects, List<FailedObject> failedObjects, List<InventoryFWPersisterResultEvent> eventsToNotify) {
        this.completedObjects = completedObjects;
        this.failedObjects = failedObjects;
        this.eventsToNotify = eventsToNotify;
    }

    public List<InventoryFWPersisterResultEvent> getEventsToNotify() {
        return eventsToNotify;
    }

    public List<NameIDType> getCompletedObjects() {
        return completedObjects;
    }

    public List<FailedObject> getFailedObjects() {
        return failedObjects;
    }
}