package in.co.sa.inventory.controller;

import in.co.sa.inventory.config.entities.InventoryDataBuilderConfig;
import in.co.sa.inventory.config.InventoryConfig;
import in.co.sa.inventory.recon.MasterReconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rca/inventory")
public class RCAController {

    @Autowired
    MasterReconService reconService;

    @Autowired
    InventoryConfig inventoryConfig;

    @GetMapping("/")
    String check() {
        return " \n \t _ : : @RCA-iN-VENTORY : : _  \n ";
    }


    @PostMapping("/recon")
    public RCAResponse recon(@RequestBody ReconRequest request) {

        String reconMode = request.getMode();
        List<String> specifications = request.getSpecifications();

        System.out.println("reconMode" + reconMode);
        System.out.println("specifications" + specifications);

        reconService.doRecon(reconMode, specifications);

        return new RCAResponse(true, "", "");
    }

    @GetMapping("/active/builders")
    public RCAResponse getActiveBuilders(){

        Map<String, InventoryDataBuilderConfig> builderConfigurations = inventoryConfig.getBuilderConfigurations();
        return new RCAResponse(true, "", builderConfigurations.keySet());

    }


    /**
     * curl -X POST http://localhost:8080/rca/inventory/recon \
     *   -H "Content-Type: application/json" \
     *   -d '{"mode":"auto", "specifications":["item1", "item2", "item3"]}'
     * */

}
