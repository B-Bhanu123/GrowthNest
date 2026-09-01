package com.fincorex.payment.event;

import com.fincorex.payment.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Payment Domain Events
 */
@Component
public class PaymentEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventHandler.class);

    public void publishPaymentCreatedEvent(PaymentDTO dto) {
        log.info("[KAFKA-EVENT] Publishing PaymentCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingPaymentMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in payment topic: {}", payload);
    }
}
