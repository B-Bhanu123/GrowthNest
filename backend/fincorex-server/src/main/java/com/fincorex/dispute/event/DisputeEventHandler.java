package com.fincorex.dispute.event;

import com.fincorex.dispute.dto.DisputeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Dispute Domain Events
 */
@Component
public class DisputeEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DisputeEventHandler.class);

    public void publishDisputeCreatedEvent(DisputeDTO dto) {
        log.info("[KAFKA-EVENT] Publishing DisputeCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingDisputeMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in dispute topic: {}", payload);
    }
}
