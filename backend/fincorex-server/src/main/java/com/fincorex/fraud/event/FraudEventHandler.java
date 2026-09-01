package com.fincorex.fraud.event;

import com.fincorex.fraud.dto.FraudDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Fraud Domain Events
 */
@Component
public class FraudEventHandler {

    private static final Logger log = LoggerFactory.getLogger(FraudEventHandler.class);

    public void publishFraudCreatedEvent(FraudDTO dto) {
        log.info("[KAFKA-EVENT] Publishing FraudCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingFraudMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in fraud topic: {}", payload);
    }
}
