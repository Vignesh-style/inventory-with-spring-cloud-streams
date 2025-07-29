package in.co.sa.inventory.config.entities;

//@JsonTypeInfo(
//        use = JsonTypeInfo.Id.NAME,
//        include = JsonTypeInfo.As.PROPERTY,
//        property = "eventType"
//)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = CustomerEvent.class, name = "CustomerEvent")
//})
public class UserEvent {
    protected String userId;
    protected String action;

    // Constructors
    public UserEvent() {}
    public UserEvent(String userId, String action) {
        this.userId = userId;
        this.action = action;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "UserEvent{userId='" + userId + "', action='" + action + "'}";
    }
}
