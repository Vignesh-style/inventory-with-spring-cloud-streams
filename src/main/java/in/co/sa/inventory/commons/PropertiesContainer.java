package in.co.sa.inventory.commons;

import java.util.Map;

public interface PropertiesContainer
{
	Map<String, Object> getProperties();
	void setProperties(Map<String, Object> properties);
}
