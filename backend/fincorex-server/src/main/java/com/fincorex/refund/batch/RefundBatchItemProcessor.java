package com.fincorex.refund.batch;

import com.fincorex.refund.dto.RefundDTO;
import com.fincorex.refund.entity.RefundRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Refund Management Ingestion & Reconcile
 */
@Component
public class RefundBatchItemProcessor implements ItemProcessor<RefundRecordEntity, RefundDTO> {

    private static final Logger log = LoggerFactory.getLogger(RefundBatchItemProcessor.class);

    @Override
    public RefundDTO process(RefundRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        RefundDTO dto = new RefundDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
