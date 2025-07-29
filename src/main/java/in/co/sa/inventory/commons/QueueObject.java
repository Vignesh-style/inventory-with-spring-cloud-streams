package in.co.sa.inventory.commons;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import in.co.nmsworks.sa.inv.config.entities.InventoryDataBuilderConfig;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)

@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleQueueObject.class, name = "SimpleQObject"),
//        @JsonSubTypes.Type(value = InventoryDataBuilderQueueObject.class, name = "InvBuilderQInput"),
//        @JsonSubTypes.Type(value = InventoryDataPersisterQueueObject.class, name = "InvPersisterQInput"),
//        @JsonSubTypes.Type(value = PostReconInvokerCommand.class, name = "PostReconInput")
})
public abstract class QueueObject implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final InventoryOperationType operationType;
    private final boolean isReconEvent;
    public InventoryDataBuilderConfig builderInformation;

    public QueueObject(InventoryOperationType operationType, boolean isReconEvent)
    {
        this.operationType = operationType;
        this.isReconEvent = isReconEvent;
    }

    public boolean isReconEvent()
    {
        return isReconEvent;
    }

    public InventoryOperationType getOperationType()
    {
        return operationType;
    }

    public boolean isPostReconCommand()
    {
        return operationType == InventoryOperationType.POST_RECON_INVOKER_COMMAND;
    }

    @Override
    public String toString()
    {
        return "QueueObject [operationType=" + operationType + "]";
    }
}