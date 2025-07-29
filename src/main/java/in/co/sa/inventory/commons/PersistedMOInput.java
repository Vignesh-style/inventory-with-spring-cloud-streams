package in.co.sa.inventory.commons;

import in.co.nmsworks.sa.inv.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistedMOInput {

    private Map<String, List<PersistedMOData>> persistedMOData;

    public void addPersistedMOData(String objectType, PersistedMOData input)
    {
        if(persistedMOData == null)
            persistedMOData = new HashMap<String, List<PersistedMOData>>();

        List<PersistedMOData> list = persistedMOData.get(objectType);
        if(list == null)
        {
            list = new ArrayList<>();
            persistedMOData.put(objectType, list);
        }

        list.add(input);
    }

    public void addAllPersistedMOData(String objectType, List<PersistedMOData> inputs)
    {
        if(persistedMOData == null)
            persistedMOData = new HashMap<String, List<PersistedMOData>>();

        List<PersistedMOData> list = persistedMOData.get(objectType);
        if(list == null)
        {
            list = new ArrayList<>();
            persistedMOData.put(objectType, list);
        }

        list.addAll(inputs);
    }

    public List<PersistedMOData> getPersistedMODataForObjectType(String objectType)
    {
        return persistedMOData == null ? new ArrayList<PersistedMOData>() : persistedMOData.get(objectType) ;
    }

    public Map<String, List<PersistedMOData>> getPersistedMOData()
    {
        return persistedMOData == null ? new HashMap<String, List<PersistedMOData>>() : persistedMOData;
    }

    public Object getUserObjectForNameIdType(NameIDType nameIDType)
    {
        if(persistedMOData == null)
            return null;

        List<PersistedMOData> list = persistedMOData.get(nameIDType.getType());
        if(Utils.isCollectionNullOrEmpty(list))
            return null;

        for (PersistedMOData persistedMOData : list)
        {
            if(persistedMOData.getNameIDType().equals(nameIDType))
                return persistedMOData.getUserObject();
        }

        return null;
    }

    public Map<NameIDType, Object> getNameIdTypeToUserObjectMapForObjectType(String objectType)
    {
        Map<NameIDType, Object> result = new HashMap<NameIDType, Object>();
        if(persistedMOData == null)
            return result;

        List<PersistedMOData> list = persistedMOData.get(objectType);
        for (PersistedMOData persistedMOData : list)
            result.put(persistedMOData.getNameIDType(), persistedMOData.getUserObject());

        return result;
    }

    public Map<String, Map<NameIDType, PersistedMOData>> getPersistedMODataMap()
    {
        Map<String, Map<NameIDType, PersistedMOData>> result = new HashMap<>();

        if(persistedMOData == null)
            return result;

        for (Map.Entry<String, List<PersistedMOData>> entry : persistedMOData.entrySet())
        {
            String objectType = entry.getKey();
            List<PersistedMOData> value = entry.getValue();

            Map<NameIDType, PersistedMOData> map = result.get(objectType);
            if(map == null)
            {
                map = new HashMap<>();
                result.put(objectType, map);
            }

            for (PersistedMOData persistedMOData : value)
            {
                map.put(persistedMOData.getNameIDType(), persistedMOData);
            }
        }

        return result;
    }

}
