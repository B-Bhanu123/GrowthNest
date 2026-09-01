package com.fincorex.investment.event;

import com.fincorex.investment.dto.InvestmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Investment Domain Events
 */
@Component
public class InvestmentEventHandler {

    private static final Logger log = LoggerFactory.getLogger(InvestmentEventHandler.class);

    public void publishInvestmentCreatedEvent(InvestmentDTO dto) {
        log.info("[KAFKA-EVENT] Publishing InvestmentCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingInvestmentMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in investment topic: {}", payload);
    }
}
