package com.fincorex.audit.event;

import com.fincorex.audit.dto.AuditDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Audit Domain Events
 */
@Component
public class AuditEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AuditEventHandler.class);

    public void publishAuditCreatedEvent(AuditDTO dto) {
        log.info("[KAFKA-EVENT] Publishing AuditCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingAuditMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in audit topic: {}", payload);
    }
}
