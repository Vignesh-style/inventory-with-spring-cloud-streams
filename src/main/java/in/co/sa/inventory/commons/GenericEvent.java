package in.co.sa.inventory.commons;

import java.io.Serializable;

public class GenericEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id = -1L;
	private String category;
	private Object userObject;
	private Integer priority = 1000; // The lesser the number, the more the priority
	private String watchCategory;

	public GenericEvent() {}

	public GenericEvent(Object userObject) {
		this.userObject = userObject;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Object getUserObject() {
        return userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    /**
     * @return priority number - lesser the number, the more the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * The lesser the number, the more the priority
     * @param priority number which denotes the event priority
     */
    public void setPriority(int priority) {
        this.priority = Integer.valueOf(priority);
    }

    public String getWatchCategory() {
        return watchCategory;
    }

    public void setWatchCategory(String watchCategory) {
		this.watchCategory = watchCategory;
	}

    @Override
    public String toString() {
        return "GenericEvent{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", userObject=" + userObject +
                ", priority=" + priority +
                ", watchCategory='" + watchCategory + '\'' +
                '}';
    }
}