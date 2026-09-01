package com.fincorex.settlement.event;

import com.fincorex.settlement.dto.SettlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Settlement Domain Events
 */
@Component
public class SettlementEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SettlementEventHandler.class);

    public void publishSettlementCreatedEvent(SettlementDTO dto) {
        log.info("[KAFKA-EVENT] Publishing SettlementCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingSettlementMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in settlement topic: {}", payload);
    }
}
