package in.co.sa.inventory;

import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.config.InventoryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;


@SpringBootApplication
@EnableScheduling
//@EnableConfigurationProperties(InventoryConfig.class)
public class RcaApplication {

    @Autowired
    InventoryConfig inventoryConfig;

	public static void main(String[] args) {
		SpringApplication.run(RcaApplication.class, args);
	}

    @EventListener(ApplicationReadyEvent.class)
    public void check(){
        System.out.println("------------------------------");
        List<InventoryDataBuilderConfig> inventoryDataBuildersList = inventoryConfig.getInventoryDataBuildersList();
        System.out.println("  " + inventoryDataBuildersList);

        if (inventoryDataBuildersList != null){
            for (InventoryDataBuilderConfig inventoryDataBuilderConfig : inventoryDataBuildersList) {
                System.out.println(inventoryDataBuilderConfig.getName());
            }
        }

        System.out.println(inventoryConfig.getBuilderConfigurations());
        System.out.println(inventoryConfig.getBuilderToReconcilerConf());
        System.out.println(inventoryConfig.getListenersConfigurations());
        System.out.println("------------------------------");
    }

}
