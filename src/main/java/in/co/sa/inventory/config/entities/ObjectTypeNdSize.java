package in.co.sa.inventory.config.entities;

public class ObjectTypeNdSize {

    private String type;
    private int batchSize;

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
