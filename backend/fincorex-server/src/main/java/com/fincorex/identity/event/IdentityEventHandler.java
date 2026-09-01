package com.fincorex.identity.event;

import com.fincorex.identity.dto.IdentityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Identity Domain Events
 */
@Component
public class IdentityEventHandler {

    private static final Logger log = LoggerFactory.getLogger(IdentityEventHandler.class);

    public void publishIdentityCreatedEvent(IdentityDTO dto) {
        log.info("[KAFKA-EVENT] Publishing IdentityCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingIdentityMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in identity topic: {}", payload);
    }
}
