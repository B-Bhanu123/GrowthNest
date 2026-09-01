package com.fincorex.analytics.batch;

import com.fincorex.analytics.dto.AnalyticsDTO;
import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Financial Analytics Engine Ingestion & Reconcile
 */
@Component
public class AnalyticsBatchItemProcessor implements ItemProcessor<AnalyticsRecordEntity, AnalyticsDTO> {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsBatchItemProcessor.class);

    @Override
    public AnalyticsDTO process(AnalyticsRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        AnalyticsDTO dto = new AnalyticsDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
