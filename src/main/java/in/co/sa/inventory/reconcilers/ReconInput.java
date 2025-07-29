package in.co.sa.inventory.reconcilers;

public class ReconInput {

    private boolean isRetriggerfailedObjects;

    public ReconInput(boolean isRetriggerfailedObjects) {
        this.isRetriggerfailedObjects = isRetriggerfailedObjects;
    }

    public boolean isRetriggerfailedObjects()
    {
        return isRetriggerfailedObjects;
    }
}
