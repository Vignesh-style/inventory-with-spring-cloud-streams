package in.co.sa.inventory.controller;

import java.util.List;

public class ReconRequest {

    private String mode;
    private List<String> specifications;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }
}
