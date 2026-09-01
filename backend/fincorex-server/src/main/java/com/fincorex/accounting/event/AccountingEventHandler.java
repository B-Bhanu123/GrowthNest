package com.fincorex.accounting.event;

import com.fincorex.accounting.dto.AccountingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Accounting Domain Events
 */
@Component
public class AccountingEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AccountingEventHandler.class);

    public void publishAccountingCreatedEvent(AccountingDTO dto) {
        log.info("[KAFKA-EVENT] Publishing AccountingCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingAccountingMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in accounting topic: {}", payload);
    }
}
