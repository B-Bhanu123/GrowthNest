package com.fincorex.admin.batch;

import com.fincorex.admin.dto.AdminDTO;
import com.fincorex.admin.entity.AdminRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Admin & Operations Center Ingestion & Reconcile
 */
@Component
public class AdminBatchItemProcessor implements ItemProcessor<AdminRecordEntity, AdminDTO> {

    private static final Logger log = LoggerFactory.getLogger(AdminBatchItemProcessor.class);

    @Override
    public AdminDTO process(AdminRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        AdminDTO dto = new AdminDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
