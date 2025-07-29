package in.co.sa.inventory.utils;

import in.co.sa.inventory.commons.InventoryEventInput;
import in.co.sa.inventory.commons.NameIDType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryBuilderUtils {


    public static Map<NameIDType, Object> getNitToEventMap(List<InventoryEventInput> events)
    {
        if(Utils.isCollectionNullOrEmpty(events))
            return Collections.emptyMap();

        Map<NameIDType, Object> result = new HashMap<NameIDType, Object>();
        for (InventoryEventInput eventInput : events) {
            result.put(eventInput.getNameIdType(), eventInput.getEvent());
        }
        return result;
    }

}