package com.fincorex.identity.batch;

import com.fincorex.identity.dto.IdentityDTO;
import com.fincorex.identity.entity.IdentityRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Identity & Access Management Ingestion & Reconcile
 */
@Component
public class IdentityBatchItemProcessor implements ItemProcessor<IdentityRecordEntity, IdentityDTO> {

    private static final Logger log = LoggerFactory.getLogger(IdentityBatchItemProcessor.class);

    @Override
    public IdentityDTO process(IdentityRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        IdentityDTO dto = new IdentityDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
