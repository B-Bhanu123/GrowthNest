package com.fincorex.merchant.batch;

import com.fincorex.merchant.dto.MerchantDTO;
import com.fincorex.merchant.entity.MerchantRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Merchant Acquiring Management Ingestion & Reconcile
 */
@Component
public class MerchantBatchItemProcessor implements ItemProcessor<MerchantRecordEntity, MerchantDTO> {

    private static final Logger log = LoggerFactory.getLogger(MerchantBatchItemProcessor.class);

    @Override
    public MerchantDTO process(MerchantRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        MerchantDTO dto = new MerchantDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
