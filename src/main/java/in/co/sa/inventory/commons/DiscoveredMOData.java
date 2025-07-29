package in.co.sa.inventory.commons;

public class DiscoveredMOData {

    private final NameIDType nameIDType;
    private final Object userObject;

    public DiscoveredMOData(NameIDType nameIDType, Object userObject)
    {
        this.nameIDType = nameIDType;
        this.userObject = userObject;
    }

    public NameIDType getNameIDType()
    {
        return nameIDType;
    }

    public Object getUserObject()
    {
        return userObject;
    }

    public String getName()
    {
        return nameIDType == null ? null : nameIDType.getName();
    }

    public String getType()
    {
        return nameIDType == null ? null : nameIDType.getType();
    }

    public Long getId()
    {
        return nameIDType == null ? null : nameIDType.getId();
    }

}
