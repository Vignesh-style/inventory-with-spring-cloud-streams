package in.co.sa.inventory.commons;

import java.io.Serializable;

public class TopologicalLinkAdditionalInfo implements AdditionalInfo, Serializable {

        /**
         *
         */
        private static final long serialVersionUID = 1L;
        private String name;
        private String type;
        private String value;
        private String property;
        private Long moID;
        private Long id;
        private Object object;


        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#setName(java.lang.String)
         */
        @Override
        public void setName(String name)
        {
            this.name = name;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#getName()
         */
        @Override
        public String getName()
        {
            return name;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#setType(java.lang.String)
         */
        @Override
        public void setType(String type)
        {
            this.type = type;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#getType()
         */
        @Override
        public String getType()
        {
            return type;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#setValue(java.lang.String)
         */
        @Override
        public void setValue(String value)
        {
            this.value = value;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#getValue()
         */
        @Override
        public String getValue()
        {
            return value;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#setProperty(java.util.Map)
         */
        @Override
        public void setProperty(String property)
        {
            this.property = property;
        }

        /* (non-Javadoc)
         * @see com.nmsworks.cygnet.telecom.elements.AdditionalInfo#getProperty()
         */
        @Override
        public String getProperty()
        {
            return property;
        }

        @Override
        public Long getMoID() {
            return moID;
        }

        @Override
        public void setMoID(Long moID) {
            this.moID = moID;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        /**
         * @return the object
         */
        public Object getObject() {
            return object;
        }

        /**
         * @param object the object to set
         */
        public void setObject(Object object) {
            this.object = object;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "TopologicalLinkAdditionalInfo [name=" + name + ", type=" + type
                    + ", value=" + value + ", property=" + property + "]";
        }


}
