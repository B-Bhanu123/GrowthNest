package com.fincorex.investment.batch;

import com.fincorex.investment.dto.InvestmentDTO;
import com.fincorex.investment.entity.InvestmentRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume Investment & Portfolio Platform Ingestion & Reconcile
 */
@Component
public class InvestmentBatchItemProcessor implements ItemProcessor<InvestmentRecordEntity, InvestmentDTO> {

    private static final Logger log = LoggerFactory.getLogger(InvestmentBatchItemProcessor.class);

    @Override
    public InvestmentDTO process(InvestmentRecordEntity item) throws Exception {
        log.trace("[SPRING-BATCH] Processing batch record ID: {}, Ref: {}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {}", item.getId());
            return null; // Skip invalid records
        }

        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }
}
