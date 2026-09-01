package com.fincorex.ledger.event;

import com.fincorex.ledger.dto.LedgerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Ledger Domain Events
 */
@Component
public class LedgerEventHandler {

    private static final Logger log = LoggerFactory.getLogger(LedgerEventHandler.class);

    public void publishLedgerCreatedEvent(LedgerDTO dto) {
        log.info("[KAFKA-EVENT] Publishing LedgerCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingLedgerMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in ledger topic: {}", payload);
    }
}
