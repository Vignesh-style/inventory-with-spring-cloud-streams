package in.co.sa.inventory.commons;

public class DAOEvent<T> extends GenericEvent {

	private static final long serialVersionUID = 1L;
	private T object = null;
	private DAOEventType type = null;
	private String[] updatedPropNames = null;
	private Object[] updatedPropOldValues = null;
	private Object[] updatedPropNewValues = null;
	
	public T getObject() {
		return object;
	}
	public void setObject(T object) {
		this.object = object;
	}
	public DAOEventType getType() {
		return type;
	}
	public void setType(DAOEventType type) {
		this.type = type;
	}
	public String[] getUpdatedPropNames() {
		return updatedPropNames;
	}
	public void setUpdatedPropNames(String[] updatedPropNames) {
		this.updatedPropNames = updatedPropNames;
	}
	public Object[] getUpdatedPropOldValues() {
		return updatedPropOldValues;
	}
	public void setUpdatedPropOldValues(Object[] updatedPropOldValues) {
		this.updatedPropOldValues = updatedPropOldValues;
	}
	public Object[] getUpdatedPropNewValues() {
		return updatedPropNewValues;
	}
	public void setUpdatedPropNewValues(Object[] updatedPropNewValues) {
		this.updatedPropNewValues = updatedPropNewValues;
	}
	
	public boolean isAddEvent()
	{
		return type == DAOEventType.ADD;
	}
	
	public boolean isUpdateEvent()
	{
		return type == DAOEventType.UPDATE;
	}
	
	public boolean isDeleteEvent()
	{
		return type == DAOEventType.DELETE;
	}

}
