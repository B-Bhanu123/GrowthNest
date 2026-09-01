package com.fincorex.settlement.batch;

import com.fincorex.settlement.dto.SettlementDTO;
import com.fincorex.settlement.entity.SettlementRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Merchant Batch Settlement Ingestion & Reconcile
 */
@Component
public class SettlementBatchItemProcessor implements ItemProcessor<SettlementRecordEntity, SettlementDTO> {

    private static final Logger log = LoggerFactory.getLogger(SettlementBatchItemProcessor.class);

    @Override
    public SettlementDTO process(SettlementRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        SettlementDTO dto = new SettlementDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
