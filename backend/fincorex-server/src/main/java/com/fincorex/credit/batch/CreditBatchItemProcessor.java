package com.fincorex.credit.batch;

import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.entity.CreditRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Credit Scoring System Ingestion & Reconcile
 */
@Component
public class CreditBatchItemProcessor implements ItemProcessor<CreditRecordEntity, CreditDTO> {

    private static final Logger log = LoggerFactory.getLogger(CreditBatchItemProcessor.class);

    @Override
    public CreditDTO process(CreditRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        CreditDTO dto = new CreditDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
