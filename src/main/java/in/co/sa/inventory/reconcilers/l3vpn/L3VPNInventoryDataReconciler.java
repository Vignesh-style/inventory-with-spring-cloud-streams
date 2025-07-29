package in.co.sa.inventory.reconcilers.l3vpn;

import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.data.builder.l3vpn.VRFENDDetails;
import in.co.sa.inventory.reconcilers.DefaultReconciler;
import in.co.sa.inventory.utils.Utils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component("L3VPNInventoryDataReconciler")
public class L3VPNInventoryDataReconciler extends DefaultReconciler
{

    @Override
    public DiscoveredMOInput getDiscoveredMOInput()
    {
        logger.info("Calling L3VPNInventoryDataReconciler");
        List<VRFENDDetails> vrfendDetails = new ArrayList<>();

        for (int i = 0; i < 5; i++){
            VRFENDDetails l3 = new VRFENDDetails();
            l3.id = (long) i;
            l3.name = "tl" + i;
            l3.type = "L3VPNVRFEnd";
            vrfendDetails.add(l3);
        }


//        String qry = "SELECT vrf.moid, mo2.name, vrfend.ID, vrfend.INTERFACENAME, mo.MOID, mo.PARENTMOID, mo.PARENTKEY, vrf.MENAME, mo1.MOID FROM L3VPNVRFEnd vrfend INNER JOIN L3VPNVRF vrf ON vrfend.L3VPNVRF_ID = vrf.moid INNER JOIN ManagedObject mo ON mo.NAME = vrfend.INTERFACENAME INNER JOIN ManagedObject mo1 ON mo1.NAME = vrf.MENAME INNER JOIN ManagedObject mo2 on mo2.moid = vrf.moid";
//
//        String[][] rows = TelecomServerUtils.sqlExecutor.executeQuery(qry);
//
//        Arrays.stream(rows).forEach(row ->
//        {
//            try {
//                Long vrfId = Utils.getLongIfBigDecimal(row[0]);
//                String vrfName = row[1];
//                Long vrfEndId = Utils.getLongIfBigDecimal(row[2]);
//                String vrfEndName = row[3];
//                Long ctpId = Utils.getLongIfBigDecimal(row[4]);
//                Long ptpId = Utils.getLongIfBigDecimal(row[5]);
//                String ptpName = row[6];
//                String type = TelecomAPIHolder.getTelecomObjectAndMapAPI().getMOType(ptpName);
//                String meName = row[7];
//                Long meId = Utils.getLongIfBigDecimal(row[8]);
//
//                VRFENDDetails details = new VRFENDDetails(vrfId, vrfName, vrfEndId, vrfEndName, ctpId, ptpId, ptpName, meId, meName, type);
//                vrfendDetails.add(details);
//            }
//            catch (Exception e)
//            {
//                logger.error("Exception", e);
//            }
//        });

        logger.info("Discovered L3VPNVRFend size {}", vrfendDetails.size());
        DiscoveredMOInput discoveredMOInput = new DiscoveredMOInput();

        vrfendDetails.forEach(end -> discoveredMOInput.addDiscoveredMOData("L3VPNVRFEnd", new DiscoveredMOData(new NameIDType(end.getVrfEndName(), end.getVrfEndId(), "L3VPNVRFEnd"), end)));

        return discoveredMOInput;
    }

    @Override
    public PersistedMOInput getPersistedMOInput()
    {
        PersistedMOInput persistedMOInput = new PersistedMOInput();
        List<VRFENDDetails> vrfendDetails = populateEndDetails();

        for (VRFENDDetails end : vrfendDetails)
            persistedMOInput.addPersistedMOData("L3VPNVRFEnd", new PersistedMOData(new NameIDType(end.getVrfEndName(), end.getVrfEndId(), "L3VPNVRFEnd"), new OperationTypeAndStatus(InventoryOperationType.ADD, RCAInventoryStatus.COMPLETED), end));

        return persistedMOInput;
    }

    private List<VRFENDDetails> populateEndDetails()
    {
        List<VRFENDDetails> vrfendDetails = new ArrayList<>();

//        String get_all_l3VPNVRFENDS = RCAHelper.getQueryForId("GET_ALL_L3VPNVRFENDS");

//        String[][] rows = TelecomServerUtils.sqlExecutor.executeQuery(get_all_l3VPNVRFENDS);
        String[][] rows = null;

        if (Utils.nullOrEmpty(rows))
        {
            logger.info("L3VPNVRFEnd details from DB is empty");
            return Collections.emptyList();
        }

        Arrays.stream(rows).forEach(row ->
        {
            Long vrfEndId = Long.valueOf(row[0]);
            String vrfEndName = row[1];
            Long vrfId = Long.valueOf(row[2]);
            String vrfName = row[3];
            Long ctpId = Long.valueOf(row[4]);
            Long ptpId = Long.valueOf(row[5]);
            String ptpName = row[6];
            String meName = row[7];
            Long meId = Long.valueOf(row[8]);
            String ptpOrFtpType = row[9];

            vrfendDetails.add(new VRFENDDetails(vrfId, vrfName, vrfEndId, vrfEndName, ctpId, ptpId, ptpName, meId, meName, ptpOrFtpType));
        });

        return vrfendDetails;
    }

}
