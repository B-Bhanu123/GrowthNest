package com.fincorex.analytics.event;

import com.fincorex.analytics.dto.AnalyticsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Analytics Domain Events
 */
@Component
public class AnalyticsEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventHandler.class);

    public void publishAnalyticsCreatedEvent(AnalyticsDTO dto) {
        log.info("[KAFKA-EVENT] Publishing AnalyticsCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingAnalyticsMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in analytics topic: {}", payload);
    }
}
