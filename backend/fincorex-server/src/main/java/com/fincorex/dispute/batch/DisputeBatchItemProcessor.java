package com.fincorex.dispute.batch;

import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.entity.DisputeRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Dispute & Chargeback Handling Ingestion & Reconcile
 */
@Component
public class DisputeBatchItemProcessor implements ItemProcessor<DisputeRecordEntity, DisputeDTO> {

    private static final Logger log = LoggerFactory.getLogger(DisputeBatchItemProcessor.class);

    @Override
    public DisputeDTO process(DisputeRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        DisputeDTO dto = new DisputeDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
