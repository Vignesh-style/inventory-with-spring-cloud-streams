package in.co.sa.inventory.commons;

import java.util.HashMap;
import java.util.Map;

public class InventoryObjectStatus implements PropertiesContainer
{
	public static final String PROPERTY_NAME_ID = "id";
	public static final String PROPERTY_NAME_OBJECT_ID = "objectId";
	public static final String PROPERTY_NAME_OBJECT_NAME = "objectName";
	public static final String PROPERTY_NAME_OBJECT_TYPE = "objectType";
	public static final String PROPERTY_NAME_STATUS = "status";
	public static final String PROPERTY_NAME_UPDATED_TIME ="updatedTime";
	public static final String PROPERTY_NAME_ERROR_REASON ="errorReason";
	public static final String PROPERTY_NAME_OPERATION_TYPE ="operationType";
	public static final String PROPERTY_NAME_ADDITIONAL_INFO ="additionalInfo";
	
	private long id;
	private long objectId;
	private String objectName;
	private String objectType;
	private String status;
	private Long updatedTime;
	private String errorReason;
	private String operationType;
	private String additionalInfo;
	
	public InventoryObjectStatus()
	{
		
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getObjectId()
	{
		return objectId;
	}

	public void setObjectId(long objectId)
	{
		this.objectId = objectId;
	}

	public String getObjectName()
	{
		return objectName;
	}

	public void setObjectName(String objectName)
	{
		this.objectName = objectName;
	}

	public String getObjectType()
	{
		return objectType;
	}

	public void setObjectType(String objectType)
	{
		this.objectType = objectType;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Long getUpdatedTime()
	{
		return updatedTime;
	}

	public void setUpdatedTime(Long updatedTime)
	{
		this.updatedTime = updatedTime;
	}

	public String getErrorReason()
	{
		return errorReason;
	}

	public void setErrorReason(String errorReason)
	{
		this.errorReason = errorReason;
	}
	
	public String getAdditionalInfo()
	{
		return additionalInfo;
	}
	
	public void setAdditionalInfo(String additionalInfo)
	{
		this.additionalInfo = additionalInfo;
	}
	
	public String getOperationType()
	{
		return operationType;
	}
	
	public void setOperationType(String operationType)
	{
		this.operationType = operationType;
	}

	@Override
	public Map<String, Object> getProperties()
	{
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put(PROPERTY_NAME_ID, id);
		mp.put(PROPERTY_NAME_OBJECT_ID, objectId);
		mp.put(PROPERTY_NAME_OBJECT_NAME, objectName);
		mp.put(PROPERTY_NAME_OBJECT_TYPE, objectType);
		mp.put(PROPERTY_NAME_ERROR_REASON, errorReason);
		mp.put(PROPERTY_NAME_UPDATED_TIME, updatedTime);
		mp.put(PROPERTY_NAME_STATUS, status);
		mp.put(PROPERTY_NAME_OPERATION_TYPE, operationType);
		mp.put(PROPERTY_NAME_ADDITIONAL_INFO, additionalInfo);
		return mp;
	}

	@Override
	public void setProperties(Map<String, Object> properties) 
	{
		setId((long) properties.get(PROPERTY_NAME_ID));
		setObjectId((long) properties.get(PROPERTY_NAME_OBJECT_ID));
		setObjectName((String) properties.get(PROPERTY_NAME_OBJECT_NAME));
		setObjectType((String) properties.get(PROPERTY_NAME_OBJECT_TYPE));
		setErrorReason((String) properties.get(PROPERTY_NAME_ERROR_REASON));
		setUpdatedTime((Long) properties.get(PROPERTY_NAME_UPDATED_TIME));
		setStatus((String) properties.get(PROPERTY_NAME_STATUS));
		setOperationType((String) properties.get(PROPERTY_NAME_OPERATION_TYPE));
		setAdditionalInfo((String) properties.get(PROPERTY_NAME_ADDITIONAL_INFO));
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (objectId ^ (objectId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryObjectStatus other = (InventoryObjectStatus) obj;
		if (objectId != other.objectId)
			return false;
		return true;
	}

	public OperationTypeAndStatus getOperationTypeAndStatus()
	{
		return new OperationTypeAndStatus(InventoryOperationType.valueOf(operationType), RCAInventoryStatus.valueOf(status));
	}


	@Override
	public String toString() {
		return "InventoryObjectStatus{" +
				"id=" + id +
				", objectId=" + objectId +
				", objectName='" + objectName + '\'' +
				", objectType='" + objectType + '\'' +
				", status='" + status + '\'' +
				", updatedTime=" + updatedTime +
				", errorReason='" + errorReason + '\'' +
				", operationType='" + operationType + '\'' +
				", additionalInfo='" + additionalInfo + '\'' +
				'}';
	}
}