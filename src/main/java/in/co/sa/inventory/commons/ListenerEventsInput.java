package in.co.sa.inventory.commons;

import in.co.nmsworks.sa.inv.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListenerEventsInput
{
	private final Map<String, List<DAOEventType>> objectTypeToEventTypesMap = new HashMap<String, List<DAOEventType>>();
		
	public void addInObjectTypeToEventTypesMap(String objectType, List<DAOEventType> eventTypes)
	{
		objectTypeToEventTypesMap.put(objectType, eventTypes);
	}
	
	public void addEvent(String objectType, DAOEventType eventType)
	{
		List<DAOEventType> list = objectTypeToEventTypesMap.get(objectType);
		if(Utils.isCollectionNullOrEmpty(list))
		{
			list = new ArrayList<>();
			objectTypeToEventTypesMap.put(objectType, list);
		}
		
		list.add(eventType);
	}
	
	public Map<String, List<DAOEventType>> getObjectTypeToEventTypesMap()
	{
		return objectTypeToEventTypesMap;
	}
	
	public List<DAOEventType> getEventTypes(String objectType)
	{
		return objectTypeToEventTypesMap.get(objectType);
	}
}