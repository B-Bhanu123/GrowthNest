package com.fincorex.upi.event;

import com.fincorex.upi.dto.UpiDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Upi Domain Events
 */
@Component
public class UpiEventHandler {

    private static final Logger log = LoggerFactory.getLogger(UpiEventHandler.class);

    public void publishUpiCreatedEvent(UpiDTO dto) {
        log.info("[KAFKA-EVENT] Publishing UpiCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingUpiMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in upi topic: {}", payload);
    }
}
