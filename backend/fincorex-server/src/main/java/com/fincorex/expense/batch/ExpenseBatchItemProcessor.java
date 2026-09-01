package com.fincorex.expense.batch;

import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.entity.ExpenseRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Corporate Expense Management Ingestion & Reconcile
 */
@Component
public class ExpenseBatchItemProcessor implements ItemProcessor<ExpenseRecordEntity, ExpenseDTO> {

    private static final Logger log = LoggerFactory.getLogger(ExpenseBatchItemProcessor.class);

    @Override
    public ExpenseDTO process(ExpenseRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
