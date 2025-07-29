package in.co.sa.inventory.commons;

public class TLProtectionData {

    private TLProtectionType tlProtectionType = null;

    public TLProtectionType getTlProtectionType() {
        return tlProtectionType;
    }

    public void setTlProtectionType(TLProtectionType tlProtectionType) {
        this.tlProtectionType = tlProtectionType;
    }

    public static enum TLProtectionType
    {
        NOT_PROTECTED, MSP, MSSP, MIXED
    }
}
