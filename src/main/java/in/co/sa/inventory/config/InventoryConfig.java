package in.co.sa.inventory.config;

import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.config.entities.ListenerConfig;
import in.co.sa.inventory.config.entities.ReconcilerConfig;
import in.co.sa.inventory.config.factory.YamlPropertySourceFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "inventory")
@PropertySource(value = "classpath:inventory-config.yml", factory = YamlPropertySourceFactory.class)
public class InventoryConfig {

    private List<InventoryDataBuilderConfig> dataBuildersList;

    private Map<String, InventoryDataBuilderConfig> builderConfigurations = new HashMap<>();
    private Map<String, List<ReconcilerConfig>> builderToReconcilerConfigurations = new HashMap<>();
    private Map<String, List<ListenerConfig>> listenersConfigurations = new HashMap<>();
    private Map<String, List<String>> listenerToBuilderMap = new HashMap<>();

    public List<InventoryDataBuilderConfig> getInventoryDataBuildersList() {
        return dataBuildersList;
    }

    public void setDataBuildersList(List<InventoryDataBuilderConfig> dataBuildersList) {
        this.dataBuildersList = dataBuildersList;
    }


    public Map<String, List<String>> getListenerToBuilderMap() {
        return listenerToBuilderMap;
    }


    public Map<String, InventoryDataBuilderConfig> getBuilderConfigurations() {
        return builderConfigurations;
    }

    public Map<String, List<ReconcilerConfig>> getBuilderToReconcilerConf() {
        return builderToReconcilerConfigurations;
    }

    public Map<String, List<ListenerConfig>> getListenersConfigurations() {
        return listenersConfigurations;
    }

    @PostConstruct
    public void populateMaps() {
        if (dataBuildersList != null) {
            for (InventoryDataBuilderConfig config : dataBuildersList) {
                String builderName = config.getName();

                System.out.println("populateMaps  -------- " + builderName);
                System.out.println(config.getReconcilers());
                System.out.println(config.getListeners());
                System.out.println("populateMaps  -------- " + builderName);

                builderConfigurations.put(builderName, config);

                if (config.getReconcilers() != null) {
                    builderToReconcilerConfigurations.put(builderName, config.getReconcilers());
                }

                if (config.getListeners() != null) {
                    listenersConfigurations.put(builderName, config.getListeners());
                }

                for (ListenerConfig listener : config.getListeners()) {
                    listenerToBuilderMap
                            .computeIfAbsent(listener.getBeanName(), k -> new ArrayList<>())
                            .add(builderName);
                }

            }
        }
    }
}
