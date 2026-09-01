package com.fincorex.gateway.event;

import com.fincorex.gateway.dto.GatewayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Gateway Domain Events
 */
@Component
public class GatewayEventHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayEventHandler.class);

    public void publishGatewayCreatedEvent(GatewayDTO dto) {
        log.info("[KAFKA-EVENT] Publishing GatewayCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingGatewayMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in gateway topic: {}", payload);
    }
}
