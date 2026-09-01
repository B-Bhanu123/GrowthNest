package com.fincorex.payment.batch;

import com.fincorex.payment.dto.PaymentDTO;
import com.fincorex.payment.entity.PaymentRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Payment Gateway Orchestration Ingestion & Reconcile
 */
@Component
public class PaymentBatchItemProcessor implements ItemProcessor<PaymentRecordEntity, PaymentDTO> {

    private static final Logger log = LoggerFactory.getLogger(PaymentBatchItemProcessor.class);

    @Override
    public PaymentDTO process(PaymentRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        PaymentDTO dto = new PaymentDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
