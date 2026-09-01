package com.fincorex.lending.event;

import com.fincorex.lending.dto.LendingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Lending Domain Events
 */
@Component
public class LendingEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LendingEventHandler.class);

    public void publishLendingCreatedEvent(LendingDTO dto) {
        log.info("[KAFKA-EVENT] Publishing LendingCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingLendingMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in lending topic: {}", payload);
    }
}
