package com.fincorex.credit.event;

import com.fincorex.credit.dto.CreditDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Credit Domain Events
 */
@Component
public class CreditEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CreditEventHandler.class);

    public void publishCreditCreatedEvent(CreditDTO dto) {
        log.info("[KAFKA-EVENT] Publishing CreditCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingCreditMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in credit topic: {}", payload);
    }
}
