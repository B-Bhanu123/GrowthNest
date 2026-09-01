package com.fincorex.refund.event;

import com.fincorex.refund.dto.RefundDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Refund Domain Events
 */
@Component
public class RefundEventHandler {

    private static final Logger log = LoggerFactory.getLogger(RefundEventHandler.class);

    public void publishRefundCreatedEvent(RefundDTO dto) {
        log.info("[KAFKA-EVENT] Publishing RefundCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingRefundMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in refund topic: {}", payload);
    }
}
