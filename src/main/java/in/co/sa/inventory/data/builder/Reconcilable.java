package in.co.sa.inventory.data.builder;

public interface Reconcilable {

    boolean isReconciliationStateActive();
    void activeReconciliation();
    void deactivateReconciliation();

}
