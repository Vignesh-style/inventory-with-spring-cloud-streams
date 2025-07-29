package in.co.sa.inventory.data.builder;

import in.co.sa.inventory.commons.InventoryDataBuilderResult;
import in.co.sa.inventory.commons.ListenerEventsInput;
import in.co.sa.inventory.commons.NameIDType;

public interface InventoryDataBuilder
{

    void beforeInit();

    void init();

    void runBuilder();

    InventoryDataBuilderResult getInventoryDataForAdd(NameIDType nit, Object userObject, boolean isreconEvent);

    boolean isValidInventoryEvent(NameIDType nit, Object userObject);

    InventoryDataBuilderResult getInventoryDataforUpdate(NameIDType nit, Object discoveredUserObject, Object persistedUserObject, boolean isreconEvent);

    InventoryDataBuilderResult getInventoryDataforDelete(NameIDType nit, Object userObject, boolean isreconEvent);

    ListenerEventsInput getListenerEventsInput();

    void shutdown();
}