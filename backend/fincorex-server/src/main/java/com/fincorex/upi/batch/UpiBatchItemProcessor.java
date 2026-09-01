package com.fincorex.upi.batch;

import com.fincorex.upi.dto.UpiDTO;
import com.fincorex.upi.entity.UpiRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume UPI Instant Transfer Network Ingestion & Reconcile
 */
@Component
public class UpiBatchItemProcessor implements ItemProcessor<UpiRecordEntity, UpiDTO> {

    private static final Logger log = LoggerFactory.getLogger(UpiBatchItemProcessor.class);

    @Override
    public UpiDTO process(UpiRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        UpiDTO dto = new UpiDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
