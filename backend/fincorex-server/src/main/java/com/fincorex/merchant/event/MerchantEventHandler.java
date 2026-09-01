package com.fincorex.merchant.event;

import com.fincorex.merchant.dto.MerchantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Merchant Domain Events
 */
@Component
public class MerchantEventHandler {

    private static final Logger log = LoggerFactory.getLogger(MerchantEventHandler.class);

    public void publishMerchantCreatedEvent(MerchantDTO dto) {
        log.info("[KAFKA-EVENT] Publishing MerchantCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingMerchantMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in merchant topic: {}", payload);
    }
}
