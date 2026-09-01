package com.fincorex.customer.event;

import com.fincorex.customer.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Customer Domain Events
 */
@Component
public class CustomerEventHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomerEventHandler.class);

    public void publishCustomerCreatedEvent(CustomerDTO dto) {
        log.info("[KAFKA-EVENT] Publishing CustomerCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingCustomerMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in customer topic: {}", payload);
    }
}
