package in.co.sa.inventory.commons;


import java.io.Serializable;

public class IDName implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final long id;
    private final String name;
    private final String key;

    public IDName(long id, String name) {
        this.id = id;
        this.name = name;
        this.key = id + "-" + name;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }


    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public long getId() {
        return id;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("IDName [id=");
        builder.append(id);
        builder.append(", ");
        if (name != null) {
            builder.append("name=");
            builder.append(name);
        }
        builder.append("]");
        return builder.toString();
    }

    public String key() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null)
            return false;

        if (!(obj instanceof IDName))
            return false;

        IDName other = (IDName) obj;
        return other.key().equals(key());
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }
}

