package in.co.sa.inventory.reconcilers;

import java.util.HashMap;
import java.util.Map;

public class ReconResult {

    private Map<String, ReconData> reconDataMap;

    public void addToReconDataMap(String objectType, ReconData recondata)
    {
        if(reconDataMap == null)
            reconDataMap = new HashMap<String, ReconData>();
        reconDataMap.put(objectType, recondata);

    }

    public Map<String, ReconData> getReconDataMap()
    {
        return reconDataMap;
    }

    public ReconData getReconData(String ObjectType)
    {
        return reconDataMap == null? null : reconDataMap.get(ObjectType);
    }
}