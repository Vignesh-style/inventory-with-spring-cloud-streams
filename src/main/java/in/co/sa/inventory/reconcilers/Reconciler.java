package in.co.sa.inventory.reconcilers;

public interface Reconciler {

    ReconResult doRecon(ReconInput reconInput);
//    void reconEventsSubmitted(String objectType, InventoryDataBuilderMaster invDataBuilderMaster);
//    void persistenceCompleted(String objectType, InventoryDataBuilderMaster invDataBuilderMaster);

}
