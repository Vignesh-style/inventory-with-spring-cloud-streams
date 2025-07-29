package in.co.sa.inventory.error.handler;

import org.springframework.messaging.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;

@Component
public class KafkaErrorHandler {

    @ServiceActivator(inputChannel = "gatekeeperIn0-in-0.errors")
    public void handleGatekeeperErrors(Message<?> message) {
        System.err.println("Error from gatekeeper-in-0: " + message);
    }
}
