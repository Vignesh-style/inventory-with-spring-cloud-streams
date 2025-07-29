package in.co.sa.inventory.persister.l3vpn;


import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.data.builder.l3vpn.VRFENDDetails;
import in.co.sa.inventory.persister.InventoryDataPersister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class L3VPNInventoryDataPersister extends InventoryDataPersister
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger();

    @Override
    public void beforeInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public InventoryDataPersisterResult doWorkForAdd(List<InventoryDataBuilderResult> userObject)
    {
        logger.info("Calling L3VPNVRF persister");
        List<NameIDType> completedObjects = new ArrayList<>();
        List<FailedObject> failedObjects = new ArrayList<>();
        List<Object[]> vrfendDetails = new ArrayList<>();
        Map<String, NameIDType> endNames = new HashMap<>();

        populateVrfEndDetails(userObject, vrfendDetails, endNames);

        logger.info("Adding TL details size {}", vrfendDetails.size());
        long startTime = System.currentTimeMillis();
        try
        {

            String insertQry = "insert into SA_INV_RCA_L3VPN_DETAILS (VRFENDID, VRFENDNAME, VRFID, VRFNAME, CTPID, PTPID, PTPNAME, PTPORFTPTYPE, MENAME, MEID) values(?,?,?,?,?,?,?,?,?,?)";
            executeBatchInChunks(insertQry, vrfendDetails, 1000, jdbcTemplate);

        }
        catch(Exception e)
        {
            logger.error("Exception while persisting L3VPNVRFEnd to database", e);
        }
        long endTime = System.currentTimeMillis();

        logger.info("Total time taken for adding L3 VPN in database - {}", endTime - startTime);

        long st = System.currentTimeMillis();

        populateCompletedAndFailedObjects(completedObjects, failedObjects, endNames);
        long et = System.currentTimeMillis();

        logger.info("Total time taken for populating completed and failed objects of L3 VPN - {}", et - st);
        return new InventoryDataPersisterResult(completedObjects, failedObjects, null);
    }

    private void populateCompletedAndFailedObjects(List<NameIDType> completedObjects, List<FailedObject> failedObjects, Map<String, NameIDType> endNames)
    {
        String qry = "Select VRFENDNAME, VRFENDID from SA_INV_RCA_L3VPN_DETAILS where VRFENDNAME IN (?)";

//        String[][] vrfEndNitsFromDB = RCAServerUtils.sqlExecutor.executePreparedInQuery(qry, "?", endNames.keySet());
        String[][] vrfEndNitsFromDB = new String[0][0];

        for(String[] nit : vrfEndNitsFromDB)
            completedObjects.add(new NameIDType(nit[0], Long.parseLong(nit[1]), "L3VPNVRFEnd"));

        List<NameIDType> failedNits = new ArrayList<>(endNames.values());
        failedNits.removeAll(completedObjects);

        for(NameIDType nit : failedNits)
            failedObjects.add(new FailedObject(nit, new OperationTypeAndStatus(InventoryOperationType.ADD, RCAInventoryStatus.PERSISTER_FAILED), "Failed while persisting in database"));

        logger.info("failedObjects size and completed objs size {} {}", failedObjects.size(), completedObjects.size());
    }

    private void populateVrfEndDetails(List<InventoryDataBuilderResult> userObject, List<Object[]> vrfendDetails, Map<String, NameIDType> endNames)
    {
        userObject.forEach( obj ->
        {
            Object inventoryData = obj.getInventoryData();

            if (Objects.isNull(inventoryData))
                return;

            VRFENDDetails endDetails = (VRFENDDetails) inventoryData;

            Object[] objects = new Object[] {endDetails.getVrfEndId(), endDetails.getVrfEndName(), endDetails.getVrfId(), endDetails.getVrfName(), endDetails.getCtpId(), endDetails.getPtpId(), endDetails.getPtpName(), endDetails.getPtpOrFTPType(), endDetails.getMeName(), endDetails.getMeId()};
            vrfendDetails.add(objects);

            endNames.put(endDetails.getVrfEndName(), new NameIDType(endDetails.getVrfEndName(), endDetails.getVrfEndId(), "L3VPNVRFEnd"));
        });
    }

    @Override
    public InventoryDataPersisterResult doWorkForUpdate(List<InventoryDataBuilderResult> userObject) {
        return null;
    }

    @Override
    public InventoryDataPersisterResult doWorkForDelete(List<InventoryDataBuilderResult> userObject)
    {
        Map<Long, NameIDType> nitMap = new HashMap<>();

        for (InventoryDataBuilderResult invDBResult : userObject)
        {
            NameIDType nameIDType = (NameIDType) invDBResult.getInventoryData();
            nitMap.put(nameIDType.getId(), nameIDType);
        }
        String deleteQry = "delete from SA_INV_RCA_L3VPN_DETAILS where VRFENDID in (?)";

        executeInClauseBatch(deleteQry, nitMap.keySet(), 1000, jdbcTemplate);

        return new InventoryDataPersisterResult(new ArrayList<>(nitMap.values()), new ArrayList<>(), null);
    }

    @Override
    public void storeStatus(List<InventoryEventInput> nitList, OperationTypeAndStatus typeAndStatus, String errorReason) {

    }

    @Override
    public void shutdown() {

    }
}