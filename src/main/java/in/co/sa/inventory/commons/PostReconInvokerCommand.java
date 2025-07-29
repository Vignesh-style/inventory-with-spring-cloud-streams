package in.co.sa.inventory.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PostReconInvokerCommand extends QueueObject
{
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(PostReconInvokerCommand.class);
	private final String reconcilerClassName;
	private final String objectType;
	private final String builderName;

	public PostReconInvokerCommand(InventoryOperationType operationType, String reconcilerClassName, String objectType, String builderName)
	{
		super(operationType, true);
		this.reconcilerClassName = reconcilerClassName;
		this.objectType = objectType;
		this.builderName = builderName;
	}

	public void invokePostRecon()
	{
//		Thread postReconThread = new Thread(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				try
//				{
//					Map<Reconciler, InventoryDataBuildersList.ReconcilerTag> reconcilersMap = InventoryBuilderUtils.getReconcilers(builderName);
//
//					if(CommonUtils.isMapNullOrEmpty(reconcilersMap))
//					{
//						logger.info("ReconcilersMap is empty for builder :{}", builderName);
//						return;
//					}
//
//					for (Entry<Reconciler, InventoryDataBuildersList.ReconcilerTag> entry : reconcilersMap.entrySet())
//					{
//						Reconciler reconciler = entry.getKey();
//						if(!reconciler.getClass().getName().equals(reconcilerClassName))
//							continue;
//
//						logger.info("Calling persistenceCompleted for reconciler :{} for builder:{}", reconcilerClassName, builderName);
//						InventoryDataBuilderMaster dataBuilderMaster = InventoryManager.getInventoryDataBuilderMaster(builderName);
//						reconciler.persistenceCompleted(objectType, dataBuilderMaster);
//					}
//				}
//				catch (Exception e)
//				{
//					logger.error("Error while calling persistenceCompleted for builder :{}, reconciler:{}", builderName, reconcilerClassName);
//					logger.error("", e);
//				}
//			}
//		});
//		postReconThread.setName("PersistenceCompleted_"+builderName);
//		postReconThread.start();

        logger.info("invoking Post Recon :: :: :: ");
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("PostReconInvokerCommand [reconcilerClassName=");
		builder.append(reconcilerClassName);
		builder.append(", objectType=");
		builder.append(objectType);
		builder.append(", builderName=");
		builder.append(builderName);
		builder.append("]");
		return builder.toString();
	}
}