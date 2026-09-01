package com.fincorex.insurance.event;

import com.fincorex.insurance.dto.InsuranceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Insurance Domain Events
 */
@Component
public class InsuranceEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InsuranceEventHandler.class);

    public void publishInsuranceCreatedEvent(InsuranceDTO dto) {
        log.info("[KAFKA-EVENT] Publishing InsuranceCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingInsuranceMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in insurance topic: {}", payload);
    }
}
