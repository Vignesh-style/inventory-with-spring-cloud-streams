package in.co.sa.inventory.data.builder.l3vpn;

import in.co.sa.inventory.commons.RCAInventoryData;

import java.io.Serializable;

public class VRFENDDetails extends RCAInventoryData implements Serializable
{
    private Long vrfId;
    private String vrfName;
    private Long ctpId;
    private Long ptpId;
    private String ptpName;
    private String ptpOrFTPType;
    private Long meId;
    private String meName;

    public VRFENDDetails() {}

    public VRFENDDetails(Long vrfId, String vrfName, Long vrfEndId, String vrfEndName, Long ctpId, Long ptpId, String ptpName, Long meId, String meName, String ptpOrFTPType)
    {
        this.vrfId = vrfId;
        this.vrfName = vrfName;
        this.id = vrfEndId;
        this.name = vrfEndName;
        this.ctpId = ctpId;
        this.ptpId = ptpId;
        this.ptpName = ptpName;
        this.meId = meId;
        this.meName = meName;
        this.ptpOrFTPType = ptpOrFTPType;
    }


    public Long getVrfId() {
        return vrfId;
    }

    public void setVrfId(Long vrfId) {
        this.vrfId = vrfId;
    }

    public String getVrfName() {
        return vrfName;
    }

    public void setVrfName(String vrfName) {
        this.vrfName = vrfName;
    }

    public Long getVrfEndId() {
        return id;
    }

    public void setVrfEndId(Long vrfEndId) {
        this.id = vrfEndId;
    }

    public String getVrfEndName() {
        return name;
    }

    public void setVrfEndName(String vrfEndName) {
        this.name = vrfEndName;
    }

    public Long getCtpId() {
        return ctpId;
    }

    public void setCtpId(Long ctpId) {
        this.ctpId = ctpId;
    }

    public Long getPtpId() {
        return ptpId;
    }

    public void setPtpId(Long ptpId) {
        this.ptpId = ptpId;
    }

    public String getPtpName() {
        return ptpName;
    }

    public void setPtpName(String ptpName) {
        this.ptpName = ptpName;
    }

    public Long getMeId() {
        return meId;
    }

    public void setMeId(Long meId) {
        this.meId = meId;
    }

    public String getMeName() {
        return meName;
    }

    public void setMeName(String meName) {
        this.meName = meName;
    }

    public String getPtpOrFTPType() {
        return ptpOrFTPType;
    }

    public void setPtpOrFTPType(String ptpOrFTPType) {
        this.ptpOrFTPType = ptpOrFTPType;
    }

    @Override
    public String toString()
    {
        return "VRFENDDetails{" +
                "vrfId=" + vrfId +
                ", vrfName='" + vrfName + '\'' +
                ", vrfEndId=" + id +
                ", vrfEndName='" + name + '\'' +
                ", ctpId=" + ctpId +
                ", ptpId=" + ptpId +
                ", ptpName='" + ptpName + '\'' +
                ", meId=" + meId +
                ", meName='" + meName + '\'' +
                '}';
    }
}