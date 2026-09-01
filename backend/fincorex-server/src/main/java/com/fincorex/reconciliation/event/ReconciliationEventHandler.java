package com.fincorex.reconciliation.event;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Reconciliation Domain Events
 */
@Component
public class ReconciliationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEventHandler.class);

    public void publishReconciliationCreatedEvent(ReconciliationDTO dto) {
        log.info("[KAFKA-EVENT] Publishing ReconciliationCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingReconciliationMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in reconciliation topic: {}", payload);
    }
}
