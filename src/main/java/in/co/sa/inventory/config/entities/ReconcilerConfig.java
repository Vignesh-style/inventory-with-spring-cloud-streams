package in.co.sa.inventory.config.entities;

public class ReconcilerConfig {

    private String BeanName;
    private boolean isReconEnabled;
    private boolean retriggerFailedObjects;

    public String getBeanName() {
        return BeanName;
    }

    public void setBeanName(String BeanName) {
        this.BeanName = BeanName;
    }

    public boolean isReconEnabled() {
        return isReconEnabled;
    }

    public void setReconEnabled(boolean reconEnabled) {
        isReconEnabled = reconEnabled;
    }

    public boolean isRetriggerFailedObjects() {
        return retriggerFailedObjects;
    }

    public void setRetriggerFailedObjects(boolean retriggerFailedObjects) {
        this.retriggerFailedObjects = retriggerFailedObjects;
    }

    @Override
    public String toString() {
        return "Reconciler{" +
                "BeanName='" + BeanName + '\'' +
                ", isReconEnabled=" + isReconEnabled +
                '}';
    }
}
