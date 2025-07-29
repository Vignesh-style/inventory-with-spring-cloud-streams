package in.co.sa.inventory.commons;

import java.io.Serializable;

public interface AdditionalInfo  extends Serializable
{

    public static final String NAME="name";
    public static final String TYPE="type";
    public static final String PROPERTY="property";
    public static final String VALUE = "value";

    public void setName(String name);
    public String getName();

    public void setType(String type);
    public String getType();

    public void setValue(String value);
    public String getValue();

    public void setProperty(String property);
    public String getProperty();

    public void setId(Long id);
    public Long getId();

    public void setMoID(Long moID);
    public Long getMoID();

    public void setObject(Object object) ;
    public Object getObject();
}