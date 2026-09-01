package com.fincorex.admin.event;

import com.fincorex.admin.dto.AdminDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Admin Domain Events
 */
@Component
public class AdminEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AdminEventHandler.class);

    public void publishAdminCreatedEvent(AdminDTO dto) {
        log.info("[KAFKA-EVENT] Publishing AdminCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingAdminMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in admin topic: {}", payload);
    }
}
