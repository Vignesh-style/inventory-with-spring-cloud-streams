package in.co.sa.inventory.commons;

public class RCAInventoryData {

    private static final long serialVersionUID = 10000000000001L;
    public static final String Name = "name";
    public static final String Id = "id";

    public Long id;
    public String name;
    public String type;

    public RCAInventoryData()
    {
    }

    public RCAInventoryData(String type)
    {
        this.type = type;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public NameIDType getNameIdType()
    {
        return new NameIDType(name, id, type);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("RCAInventoryData{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
