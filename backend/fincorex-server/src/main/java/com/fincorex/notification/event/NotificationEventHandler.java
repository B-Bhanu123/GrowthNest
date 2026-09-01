package com.fincorex.notification.event;

import com.fincorex.notification.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Notification Domain Events
 */
@Component
public class NotificationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventHandler.class);

    public void publishNotificationCreatedEvent(NotificationDTO dto) {
        log.info("[KAFKA-EVENT] Publishing NotificationCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingNotificationMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in notification topic: {}", payload);
    }
}
