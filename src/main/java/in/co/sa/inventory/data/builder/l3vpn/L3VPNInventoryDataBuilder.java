package in.co.sa.inventory.data.builder.l3vpn;

import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.data.builder.InventoryDataBuilder;
import in.co.sa.inventory.data.builder.Reconcilable;
import in.co.sa.inventory.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("L3VPNInventoryDataBuilder")
public class L3VPNInventoryDataBuilder implements InventoryDataBuilder, Reconcilable
{
    private static final Logger logger = LogManager.getLogger();

    private static String RECONCILIATION_STATE = "Non-Active";


    @Override
    public boolean isReconciliationStateActive() {
        return RECONCILIATION_STATE.equals("Active");
    }

    @Override
    public void activeReconciliation() {
        RECONCILIATION_STATE = "Active";
    }

    @Override
    public void deactivateReconciliation() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RECONCILIATION_STATE = "Non-Active";
    }

    @Override
    public void beforeInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void runBuilder() {

    }

    @Override
    public InventoryDataBuilderResult getInventoryDataForAdd(NameIDType nit, Object userObject, boolean isreconEvent)
    {
        logger.info("Calling L3vpnvrf builder for {} {} {}", nit, isreconEvent, userObject.getClass());
        try
        {
            VRFENDDetails end = null;

            if(isreconEvent)
                end = (VRFENDDetails) userObject;

            else
            {
                L3VPNVRFEnd l3VPNVRFEnd = (L3VPNVRFEnd) userObject;
                end = constructVRFEndDetails(l3VPNVRFEnd);
            }

            return new InventoryDataBuilderResult(nit, new OperationTypeAndStatus(InventoryOperationType.ADD, RCAInventoryStatus.COMPLETED), end, true, null);
        }
        catch (Exception e)
        {
            logger.error("Exception occurred while building L3VPNVRFEnd for addition for {}", nit, e);
            return new InventoryDataBuilderResult(nit, new OperationTypeAndStatus(InventoryOperationType.ADD, RCAInventoryStatus.BUILDER_FAILED), null, false, null);
        }
    }

    private VRFENDDetails constructVRFEndDetails(L3VPNVRFEnd l3VPNVRFEnd) throws Exception
    {
        String qry = "SELECT vrf.ID, vrf.VRF_NAME, vrfend.ID, vrfend.INTERFACENAME, mo.MOID, mo.PARENTMOID, mo.PARENTKEY, vrf.MENAME, mo1.MOID FROM L3VPNVRFEnd vrfend INNER JOIN L3VPNVRF vrf ON vrfend.INTERFACENAME = ? and vrfend.L3VPNVRF_ID = vrf.ID INNER JOIN ManagedObject mo ON mo.NAME = vrfend.INTERFACENAME INNER JOIN ManagedObject mo1 ON mo1.NAME = vrf.MENAME";
//        String[][] rows = TelecomServerUtils.sqlExecutor.executePreparedQuery(qry, l3VPNVRFEnd.getInterfaceName());
        String[][] rows = null;

        if (Utils.nullOrEmpty(rows))
            return null;

        String[] row = rows[0];

        Long vrfId = Utils.getLongIfBigDecimal(row[0]);
        String vrfName = row[1];
        Long vrfEndId = Utils.getLongIfBigDecimal( row[2]);
        String vrfEndName = row[3];
        Long ctpId = Utils.getLongIfBigDecimal( row[4]);
        Long ptpId = Utils.getLongIfBigDecimal( row[5]);
        String ptpName = row[6];
        String meName = row[7];
        Long meId = Utils.getLongIfBigDecimal( row[8]);
        String moType = "l3";

        return new VRFENDDetails(vrfId, vrfName, vrfEndId, vrfEndName, ctpId, ptpId, ptpName, meId, meName, moType);

    }

    @Override
    public boolean isValidInventoryEvent(NameIDType nit, Object userObject)
    {
        return "L3VPNVRFEnd".equals(nit.getType());
    }

    @Override
    public InventoryDataBuilderResult getInventoryDataforUpdate(NameIDType nit, Object discoveredUserObject, Object persistedUserObject, boolean isreconEvent) {
        return null;
    }

    @Override
    public InventoryDataBuilderResult getInventoryDataforDelete(NameIDType nit, Object userObject, boolean isreconEvent)
    {
        return new InventoryDataBuilderResult(nit, new OperationTypeAndStatus(InventoryOperationType.DELETE, RCAInventoryStatus.COMPLETED), nit, true, null);
    }

    @Override
    public ListenerEventsInput getListenerEventsInput()
    {
        ListenerEventsInput listenerEventsInput = new ListenerEventsInput();

        List<DAOEventType> addUpdateDelete = new ArrayList<>();
        addUpdateDelete.add(DAOEventType.ADD);
        addUpdateDelete.add(DAOEventType.DELETE);
        addUpdateDelete.add(DAOEventType.UPDATE);

        listenerEventsInput.addInObjectTypeToEventTypesMap("L3VPNVRFEnd", addUpdateDelete);

        return listenerEventsInput;
    }

    @Override
    public void shutdown() {

    }
}