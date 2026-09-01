package com.fincorex.reconciliation.batch;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Automated Bank Reconciliation Ingestion & Reconcile
 */
@Component
public class ReconciliationBatchItemProcessor implements ItemProcessor<ReconciliationRecordEntity, ReconciliationDTO> {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationBatchItemProcessor.class);

    @Override
    public ReconciliationDTO process(ReconciliationRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        ReconciliationDTO dto = new ReconciliationDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
