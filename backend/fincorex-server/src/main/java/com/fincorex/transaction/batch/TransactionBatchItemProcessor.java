package com.fincorex.transaction.batch;

import com.fincorex.transaction.dto.TransactionDTO;
import com.fincorex.transaction.entity.TransactionRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Transaction Processing Core Ingestion & Reconcile
 */
@Component
public class TransactionBatchItemProcessor implements ItemProcessor<TransactionRecordEntity, TransactionDTO> {

    private static final Logger log = LoggerFactory.getLogger(TransactionBatchItemProcessor.class);

    @Override
    public TransactionDTO process(TransactionRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
