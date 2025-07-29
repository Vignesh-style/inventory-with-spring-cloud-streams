package in.co.sa.inventory.config.entities;

import java.util.List;

public class InventoryDataBuilderConfig {

    private String name;
    private String builderBeanName;
    private String persisterBeanName;
    private String channelName;
    private boolean isInventoryBuildingEnabled;
    private int dataBuilderThreadPoolSize;
    private List<ReconcilerConfig> reconcilers;
    private List<ListenerConfig> listeners;
    private List<ObjectTypeNdSize> objectTypeSequence;
    private CustomInput customInput;

    public String getBuilderBeanName() {
        return builderBeanName;
    }

    public void setBuilderBeanName(String builderBeanName) {
        this.builderBeanName = builderBeanName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public CustomInput getCustomInput() {
        return customInput;
    }

    public void setCustomInput(CustomInput customInput) {
        this.customInput = customInput;
    }

    public int getDataBuilderThreadPoolSize() {
        return dataBuilderThreadPoolSize;
    }

    public void setDataBuilderThreadPoolSize(int dataBuilderThreadPoolSize) {
        this.dataBuilderThreadPoolSize = dataBuilderThreadPoolSize;
    }

    public boolean isInventoryBuildingEnabled() {
        return isInventoryBuildingEnabled;
    }

    public void setInventoryBuildingEnabled(boolean inventoryBuildingEnabled) {
        isInventoryBuildingEnabled = inventoryBuildingEnabled;
    }

    public List<ListenerConfig> getListeners() {
        return listeners;
    }

    public void setListeners(List<ListenerConfig> listenerConfigs) {
        this.listeners = listenerConfigs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectTypeNdSize> getObjectTypeSequence() {
        return objectTypeSequence;
    }

    public void setObjectTypeSequence(List<ObjectTypeNdSize> objectTypeSequence) {
        this.objectTypeSequence = objectTypeSequence;
    }

    public String getPersisterBeanName() {
        return persisterBeanName;
    }

    public void setPersisterBeanName(String persisterBeanName) {
        this.persisterBeanName = persisterBeanName;
    }

    public List<ReconcilerConfig> getReconcilers() {
        return reconcilers;
    }

    public void setReconcilers(List<ReconcilerConfig> reconcilerConfigs) {
        this.reconcilers = reconcilerConfigs;
    }
}
