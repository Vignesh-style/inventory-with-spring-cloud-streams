package in.co.sa.inventory.config.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(resource.getResource());
        Properties properties = factory.getObject();

        // Debug log loaded props
        if (properties != null) {
            properties.forEach((k, v) -> System.out.println("Loaded property: " + k + " = " + v));
        } else {
            System.out.println("No properties loaded from: " + resource.getResource().getFilename());
        }

        return new PropertiesPropertySource(resource.getResource().getFilename(), properties);
    }
}
