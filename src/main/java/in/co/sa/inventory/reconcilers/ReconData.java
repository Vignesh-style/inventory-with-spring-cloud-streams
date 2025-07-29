package in.co.sa.inventory.reconcilers;

import in.co.sa.inventory.commons.InventoryEventInput;
import in.co.sa.inventory.commons.ReconUpdateInventoryEventInput;
import in.co.sa.inventory.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ReconData {

    List<InventoryEventInput> addList;
    List<ReconUpdateInventoryEventInput> updateList;
    List<InventoryEventInput> deleteList;


    public void addToAddList(List<InventoryEventInput> add) {
        if(addList == null)
            addList = new ArrayList<>();

        addList.addAll(add);
    }

    public void addToUpdateList(List<ReconUpdateInventoryEventInput> update) {
        if(updateList == null)
            updateList = new ArrayList<>();

        updateList.addAll(update);
    }

    public void addToDelList(List<InventoryEventInput> del) {
        if(deleteList == null)
            deleteList = new ArrayList<>();

        deleteList.addAll(del);
    }

    public List<InventoryEventInput> getAddList() {
        return addList;
    }

    public List<ReconUpdateInventoryEventInput> getUpdateList() {
        return updateList;
    }

    public List<InventoryEventInput> getDeleteList() {
        return deleteList;
    }

    public boolean isAddListEmpty()
    {
        return Utils.isCollectionNullOrEmpty(addList);
    }

    public boolean isDeleteListEmpty()
    {
        return Utils.isCollectionNullOrEmpty(deleteList);
    }

    public boolean isUpdateListEmpty()
    {
        return Utils.isCollectionNullOrEmpty(updateList);
    }

    public void addToAddList(InventoryEventInput add)
    {
        if(addList == null)
            addList = new ArrayList<>();

        addList.add(add);
    }

    public void addToDelList(InventoryEventInput del)
    {
        if(deleteList == null)
            deleteList = new ArrayList<>();

        deleteList.add(del);
    }

    public void addToUpdateList(ReconUpdateInventoryEventInput reconUpdateInventoryEvent)
    {
        if(updateList == null)
            updateList = new ArrayList<>();

        updateList.add(reconUpdateInventoryEvent);
    }
}

