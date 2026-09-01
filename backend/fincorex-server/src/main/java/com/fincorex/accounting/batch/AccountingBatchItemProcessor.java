package com.fincorex.accounting.batch;

import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.entity.AccountingRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume General Accounting & Trial Balance Ingestion & Reconcile
 */
@Component
public class AccountingBatchItemProcessor implements ItemProcessor<AccountingRecordEntity, AccountingDTO> {

    private static final Logger log = LoggerFactory.getLogger(AccountingBatchItemProcessor.class);

    @Override
    public AccountingDTO process(AccountingRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        AccountingDTO dto = new AccountingDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
