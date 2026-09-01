package com.fincorex.transaction.event;

import com.fincorex.transaction.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Transaction Domain Events
 */
@Component
public class TransactionEventHandler {

    private static final Logger log = LoggerFactory.getLogger(TransactionEventHandler.class);

    public void publishTransactionCreatedEvent(TransactionDTO dto) {
        log.info("[KAFKA-EVENT] Publishing TransactionCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingTransactionMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in transaction topic: {}", payload);
    }
}
