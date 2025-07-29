package in.co.sa.inventory.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

public class L3VPNVRFEnd implements Serializable, Cloneable {

    private static final long serialVersionUID = -7493770058087214579L;

    private static final transient Logger logger = LogManager.getLogger();

    public static final String INTERFACENAME = "interfaceName";
    public static final String CIRCUITID = "circuitId";
    public static final String ADDITIONALINFO = "additionalInfo";

    private Long id;
    private String interfaceName;
    private String circuitId;
    private String ipAddress;
    private String subnetMask;
    private String svlan;
    private String cvlan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getCircuitId() {
        return circuitId;
    }

    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }

    public String getSvlan() {
        return svlan;
    }

    public void setSvlan(String svlan) {
        this.svlan = svlan;
    }

    public String getCvlan() {
        return cvlan;
    }

    public void setCvlan(String cvlan) {
        this.cvlan = cvlan;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        L3VPNVRFEnd that = (L3VPNVRFEnd) o;

        if (interfaceName != null ? !interfaceName.equals(that.interfaceName) : that.interfaceName != null)
            return false;
        if (circuitId != null ? !circuitId.equals(that.circuitId) : that.circuitId != null) return false;
        if (ipAddress != null ? !ipAddress.equals(that.ipAddress) : that.ipAddress != null) return false;
        if (subnetMask != null ? !subnetMask.equals(that.subnetMask) : that.subnetMask != null) return false;
        if (svlan != null ? !svlan.equals(that.svlan) : that.svlan != null) return false;
        if (cvlan != null ? !cvlan.equals(that.cvlan) : that.cvlan != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = interfaceName != null ? interfaceName.hashCode() : 0;
        result = 31 * result + (circuitId != null ? circuitId.hashCode() : 0);
        result = 31 * result + (ipAddress != null ? ipAddress.hashCode() : 0);
        result = 31 * result + (subnetMask != null ? subnetMask.hashCode() : 0);
        result = 31 * result + (svlan != null ? svlan.hashCode() : 0);
        result = 31 * result + (cvlan != null ? cvlan.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "L3VPNVRFEnd{" +
                "id=" + id +
                ", interfaceName='" + interfaceName + '\'' +
                ", circuitId='" + circuitId + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", subnetMask='" + subnetMask + '\'' +
                ", svlan='" + svlan + '\'' +
                ", cvlan='" + cvlan + '\'' +
                '}';
    }

}