package in.co.sa.inventory.config.entities;

public class CustomerEvent extends UserEvent {

    int custId;

    public CustomerEvent() {
    }

    public CustomerEvent(int custId) {
        this.custId = custId;
    }

    public CustomerEvent(String userId, String action, int custId) {
        super(userId, action);
        this.custId = custId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    @Override
    public String toString() {
        return "CustomerEvent{" +
                "custId=" + custId +
                ", action='" + action + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
