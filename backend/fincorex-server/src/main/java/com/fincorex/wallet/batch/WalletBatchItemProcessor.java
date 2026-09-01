package com.fincorex.wallet.batch;

import com.fincorex.wallet.dto.WalletDTO;
import com.fincorex.wallet.entity.WalletRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Stored-Value Digital Wallet Ingestion & Reconcile
 */
@Component
public class WalletBatchItemProcessor implements ItemProcessor<WalletRecordEntity, WalletDTO> {

    private static final Logger log = LoggerFactory.getLogger(WalletBatchItemProcessor.class);

    @Override
    public WalletDTO process(WalletRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        WalletDTO dto = new WalletDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
