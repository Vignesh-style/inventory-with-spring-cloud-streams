package in.co.sa.inventory.persister;

import in.co.sa.inventory.commons.*;
import in.co.sa.inventory.utils.Utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rocksdb.OperationType;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class InventoryDataPersister {
    static final Logger logger = LogManager.getLogger();

    // Method will be called for any value of 'isInventoryBuildingEnabled' boolean.
    abstract protected void beforeInit();

    // Method will be called iff 'isInventoryBuildingEnabled' boolean is true.
    abstract protected void init();

    abstract protected InventoryDataPersisterResult doWorkForAdd(List<InventoryDataBuilderResult> userObject);

    abstract protected InventoryDataPersisterResult doWorkForUpdate(List<InventoryDataBuilderResult> userObject);

    abstract protected InventoryDataPersisterResult doWorkForDelete(List<InventoryDataBuilderResult> userObject);

    abstract protected void storeStatus(List<InventoryEventInput> nitList, OperationTypeAndStatus typeAndStatus, String errorReason);

    abstract protected void shutdown();

    private Map<String, List<InventoryDbPersistData>> populateDataOverOperationTypeAndStatus(List<InventoryDbPersistData> data) {

        Map<String, List<InventoryDbPersistData>> res = new HashMap<>();

        for (InventoryDbPersistData d : data) {
            List<InventoryDbPersistData> dataList = res.computeIfAbsent(d.getOperationTypeAndStatus().getKey(), k -> new ArrayList<>());
            dataList.add(d);
        }

        return res;
    }

    protected void updateState(List<InventoryDbPersistData> dataList) {

        //todo have to change as per the new Builders logic.

        if (true) {
            logger.info("Updating status in the DB");
            return;
        }

        Map<String, List<InventoryDbPersistData>> map = populateDataOverOperationTypeAndStatus(dataList);

        logger.debug("populateDataOverOperationTypeAndStatus : {}", map.keySet());

        if (Utils.nullOrEmpty(dataList))
            return;

        map.forEach((operationTypeAndStatus, dList) -> {

            Set<Long> deleteMoids = new HashSet<>();
            List<InventoryObjectStatus> inventoryObjectList = new ArrayList<>();
            List<InventoryEventInput> nitList = new ArrayList<>();

            for (InventoryDbPersistData dl : dList) {

                if (dl.getEventInput().getNameIdType().getType().equals(Constants.ObjectType.PGP.name()))
                    continue;

                nitList.add(dl.getEventInput());

                if (dl.getOperationTypeAndStatus().getOperationType() == InventoryOperationType.DELETE && dl.getOperationTypeAndStatus().getStatus() == RCAInventoryStatus.COMPLETED) {

                    NameIDType nameIdType = dl.getEventInput().getNameIdType();
                    deleteMoids.add(nameIdType.getId());
                    continue;

                }

                InventoryObjectStatus inventoryObject = new InventoryObjectStatus();
                NameIDType nameIDType = dl.getEventInput().getNameIdType();
                inventoryObject.setObjectId(nameIDType.getId());
                inventoryObject.setObjectName(nameIDType.getName());
                inventoryObject.setObjectType(nameIDType.getType());
                inventoryObject.setStatus(dl.getOperationTypeAndStatus().getStatus().name());
                inventoryObject.setOperationType(dl.getOperationTypeAndStatus().getOperationType().name());
                OperationType opType = (OperationType) dl.getOperationTypeAndStatus().getAdditionalInfo();
                inventoryObject.setAdditionalInfo(opType == null ? null : opType.name());
                if (dl.getErrorReason() != null) {
                    inventoryObject.setErrorReason(dl.getErrorReason().length() > 255 ? dl.getErrorReason().substring(0, 250) : dl.getErrorReason());
                }
                long updatedTime = System.currentTimeMillis();
                inventoryObject.setUpdatedTime(updatedTime);
                inventoryObjectList.add(inventoryObject);
            }

            logger.info("to delete ::  {}", deleteMoids.size());
            logger.info("to aDD ::  {}", inventoryObjectList.size());
            logger.info("total ::  {}", nitList.size());

            String[] keySplit = operationTypeAndStatus.split("\\$");
            OperationTypeAndStatus typeAndStatus = new OperationTypeAndStatus(InventoryOperationType.valueOf(keySplit[0]), RCAInventoryStatus.valueOf(keySplit[1]));

//            if (Utils.hasElement(deleteMoids))
//                deleteObjectsFromMysql(deleteMoids, nitList, typeAndStatus);
//
//            if (Utils.hasElement(inventoryObjectList))
//                addObjectsToMysql(inventoryObjectList, nitList, typeAndStatus);

        });

    }

//    public synchronized void addObjectsToMysql(List<InventoryObjectStatus> inventoryObjectList, List<InventoryEventInput> nitList, OperationTypeAndStatus operationTypeAndStatus) {
//        InvWriter.addObjectsToMysqlDB(inventoryObjectList, nitList, operationTypeAndStatus, rcaInvObjectCountManager, getTechnology());
//    }
//
//    public synchronized void deleteObjectsFromMysql(Set<Long> moids, List<InventoryEventInput> nitList, OperationTypeAndStatus operationTypeAndStatus) {
//        InvWriter.deleteObjectsFromMysqlDB(moids, nitList, operationTypeAndStatus, rcaInvObjectCountManager, getTechnology());
//    }

    /**
     * Executes batch inserts/updates in chunks to prevent memory or packet overflows.
     *
     * @param sql        The parameterized SQL query.
     * @param batchParams List of Object[] representing each row.
     * @param chunkSize  Max records per batch (e.g., 1000).
     */
    public void executeBatchInChunks(String sql, List<Object[]> batchParams, int chunkSize, JdbcTemplate jdbcTemplate) {
        if (batchParams == null || batchParams.isEmpty()) {
            return;
        }

        for (int i = 0; i < batchParams.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, batchParams.size());
            List<Object[]> chunk = batchParams.subList(i, end);

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int rowIndex) throws SQLException {
                    Object[] params = chunk.get(rowIndex);
                    for (int j = 0; j < params.length; j++) {
                        ps.setObject(j + 1, params[j]);
                    }
                }

                @Override
                public int getBatchSize() {
                    return chunk.size();
                }
            });
        }
    }

    public void executeInClauseBatch(String baseSql, Collection<?> values, int chunkSize, JdbcTemplate jdbcTemplate) {
        if (values == null || values.isEmpty()) return;

        List<?> valueList = new ArrayList<>(values);

        for (int i = 0; i < valueList.size(); i += chunkSize) {
            int end = Math.min(i + chunkSize, valueList.size());
            List<?> subList = valueList.subList(i, end);

            // Build (?, ?, ?, ...) placeholders
            String placeholders = subList.stream().map(v -> "?").collect(Collectors.joining(","));
            String finalSql = baseSql.replace("IN (?)", "IN (" + placeholders + ")");

            jdbcTemplate.update(finalSql, subList.toArray());
        }
    }
}
