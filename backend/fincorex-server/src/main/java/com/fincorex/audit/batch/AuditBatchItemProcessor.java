package com.fincorex.audit.batch;

import com.fincorex.audit.dto.AuditDTO;
import com.fincorex.audit.entity.AuditRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Immutable Audit Logging Ingestion & Reconcile
 */
@Component
public class AuditBatchItemProcessor implements ItemProcessor<AuditRecordEntity, AuditDTO> {

    private static final Logger log = LoggerFactory.getLogger(AuditBatchItemProcessor.class);

    @Override
    public AuditDTO process(AuditRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        AuditDTO dto = new AuditDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
