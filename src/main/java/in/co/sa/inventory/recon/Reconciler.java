package in.co.sa.inventory.recon;

import org.springframework.cloud.stream.function.StreamBridge;

import java.util.HashMap;
import java.util.Map;

public class Reconciler {

    private final StreamBridge streamBridge;

    public Reconciler(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    //    @Scheduled(fixedDelay = 20000, initialDelay = 20000)
    public void sendReconTrigger() {
        Map<String, Object> reconData = new HashMap<>();
        reconData.put("type", "RECON");
        reconData.put("builderId", "builder1");
        reconData.put("payload", "Recon data run at " + System.currentTimeMillis());

        System.out.println("sendReconTrigger -- recon :: " + reconData);

        streamBridge.send("reconTrigger-out-0", reconData);
    }

}

/**
 *  THE SAMPLE FOR SCS UNDERSTANDING
 * */