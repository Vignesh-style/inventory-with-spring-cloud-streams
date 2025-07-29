package in.co.sa.inventory.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoveredMOInput {

    private Map<String, List<DiscoveredMOData>> discoveredMOData;

    public void addDiscoveredMOData(String objectType, DiscoveredMOData moData)
    {
        if(discoveredMOData == null)
            discoveredMOData = new HashMap<String, List<DiscoveredMOData>>();

        List<DiscoveredMOData> list = discoveredMOData.get(objectType);
        if(list == null)
        {
            list = new ArrayList<>();
            discoveredMOData.put(objectType, list);
        }

        list.add(moData);
    }

    public void addAllDiscoveredMOData(String objectType, List<DiscoveredMOData> moData)
    {
        if(discoveredMOData == null)
            discoveredMOData = new HashMap<String, List<DiscoveredMOData>>();

        List<DiscoveredMOData> list = discoveredMOData.get(objectType);
        if(list == null)
        {
            list = new ArrayList<>();
            discoveredMOData.put(objectType, list);
        }

        list.addAll(moData);
    }

    public List<DiscoveredMOData> getDiscoveredMODataForObjectType(String objectType)
    {
        return discoveredMOData == null ? new ArrayList<DiscoveredMOData>() : discoveredMOData.get(objectType);
    }

    public Map<String, List<DiscoveredMOData>> getDiscoveredMOData()
    {
        return discoveredMOData == null ? new HashMap<String, List<DiscoveredMOData>>() : discoveredMOData;
    }

    public Map<NameIDType, Object> getNameIdTypeToUserObjectMapForObjectType(String objectType)
    {
        Map<NameIDType, Object> result = new HashMap<NameIDType, Object>();
        if(discoveredMOData == null)
            return result;

        List<DiscoveredMOData> list = discoveredMOData.get(objectType);
        for (DiscoveredMOData discoveredMOData : list)
            result.put(discoveredMOData.getNameIDType(), discoveredMOData.getUserObject());

        return result;
    }

    public Map<String, Map<NameIDType, Object>> getDiscoveredMODataMap()
    {
        Map<String, Map<NameIDType, Object>> result = new HashMap<>();

        if(discoveredMOData == null)
            return result;

        for (Map.Entry<String, List<DiscoveredMOData>> entry : discoveredMOData.entrySet())
        {
            String key = entry.getKey();
            List<DiscoveredMOData> value = entry.getValue();

            Map<NameIDType, Object> map = result.get(key);
            if(map == null)
            {
                map = new HashMap<>();
                result.put(key, map);
            }

            for (DiscoveredMOData discoveredMOData : value)
            {
                map.put(discoveredMOData.getNameIDType(), discoveredMOData.getUserObject());
            }
        }

        return result;
    }

}
