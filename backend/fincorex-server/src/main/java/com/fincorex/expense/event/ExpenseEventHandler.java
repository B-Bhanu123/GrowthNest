package com.fincorex.expense.event;

import com.fincorex.expense.dto.ExpenseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Expense Domain Events
 */
@Component
public class ExpenseEventHandler {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEventHandler.class);

    public void publishExpenseCreatedEvent(ExpenseDTO dto) {
        log.info("[KAFKA-EVENT] Publishing ExpenseCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingExpenseMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in expense topic: {}", payload);
    }
}
