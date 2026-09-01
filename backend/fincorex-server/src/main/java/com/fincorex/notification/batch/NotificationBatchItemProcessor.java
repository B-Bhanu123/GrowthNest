package com.fincorex.notification.batch;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.entity.NotificationRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Centralized Notification System Ingestion & Reconcile
 */
@Component
public class NotificationBatchItemProcessor implements ItemProcessor<NotificationRecordEntity, NotificationDTO> {

    private static final Logger log = LoggerFactory.getLogger(NotificationBatchItemProcessor.class);

    @Override
    public NotificationDTO process(NotificationRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        NotificationDTO dto = new NotificationDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
