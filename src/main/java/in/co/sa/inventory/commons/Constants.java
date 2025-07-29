package in.co.sa.inventory.commons;

public class Constants {

    public enum ObjectType
    {
        TransportEMS,
        ME,
        VNE,
        VirtualNetwork,
        PTP,
        FTP,
        CTP,
        EquipmentHolder,
        Equipment,
        PGP,
        EPGP,
        MultiLayerSubnetwork,
        TopologicalLink,
        SNC,
        BandwidthService,
        ConnectionlessService,
        Container,
        Ring,
        MECrossConnect,
        LinkGroup,
        FlowDomain,
        FlowDomainFragment,
        MatrixFlowDomain,
        RoutingLink,
        RoutingLine,
        CurrentMaintenanceOperation,
        SwitchData,
        VirtualLink,
        VirtualCrossConnect,
        VirtualConnection,
        Topology,
        TunnelPGP,
        LinkProtectionGroup,
        SRLG,
        CFC,
        NFC,
        Service,
        SpliceClosure,
        NetworkProtection,
        MPLSTopology,
        MFDFr,
        Route,
        TunnelFdfrMapping,
        L3VPN,
        L3VPNVRF,
        LAG,
        RoutingArea,
        SnppLink,
        TrailNetworkProtection,
        MaintenanceDomain,
        MaintenanceAssociation,
        MaintenanceEndPoint,
        WDMPGP,
        NETime,
        /*
        Added Submarine Objects in Core, as it is a enum it can't be extended in submarine module.
        so added it here. In future it has to be segregated.
         */
        BU,
        FiberPair,
        Rack,
        SubmarineODF,
        Section,
        Segment,
        Station,
        RSTrail,
        Splitter,
        Wavelength,
        BWSWrapper,
        CLSWrapper,
        CableSystem,
        MicrowavePGP,
        Layer3Link,
        TPE,
        FRE,
        CableHead,
        CentralOffice,
        SpokeChannel,
        Connection
    }

    public enum TLEndTPType
    {
        PTP,
        FTP
    }

    public enum MEVNETLType
    {
        ME_TO_ME,
        ME_TO_VNE,
        VNE_TO_VNE
    }


    public enum TLPhyLogType
    {
        PHYSICAL,
        LOGICAL,
        NA
    }
}
