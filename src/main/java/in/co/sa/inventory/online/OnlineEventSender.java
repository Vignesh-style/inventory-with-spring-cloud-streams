package in.co.sa.inventory.online;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OnlineEventSender {

    private final StreamBridge streamBridge;

    public OnlineEventSender(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

//    @Scheduled(fixedDelay = 20000, initialDelay = 1000)
    public void sendOnlineEvent() {
        Map<String, Object> onlineData = new HashMap<>();
        onlineData.put("type", "ONLINE");
        onlineData.put("builderId", "builder1");
        onlineData.put("payload", "Online event at " + System.currentTimeMillis());

        System.out.println("sending online data : " + onlineData);

        streamBridge.send("onlineSender-out-0", onlineData);
    }
}


/**
 *  THE SAMPLE FOR SCS UNDERSTANDING
 * */