package com.fincorex.fraud.batch;

import com.fincorex.fraud.dto.FraudDTO;
import com.fincorex.fraud.entity.FraudRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Real-Time Fraud Detection Engine Ingestion & Reconcile
 */
@Component
public class FraudBatchItemProcessor implements ItemProcessor<FraudRecordEntity, FraudDTO> {

    private static final Logger log = LoggerFactory.getLogger(FraudBatchItemProcessor.class);

    @Override
    public FraudDTO process(FraudRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        FraudDTO dto = new FraudDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
