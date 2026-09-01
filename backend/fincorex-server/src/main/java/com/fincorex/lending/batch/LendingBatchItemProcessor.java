package com.fincorex.lending.batch;

import com.fincorex.lending.dto.LendingDTO;
import com.fincorex.lending.entity.LendingRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Lending & Underwriting Engine Ingestion & Reconcile
 */
@Component
public class LendingBatchItemProcessor implements ItemProcessor<LendingRecordEntity, LendingDTO> {

    private static final Logger log = LoggerFactory.getLogger(LendingBatchItemProcessor.class);

    @Override
    public LendingDTO process(LendingRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        LendingDTO dto = new LendingDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
