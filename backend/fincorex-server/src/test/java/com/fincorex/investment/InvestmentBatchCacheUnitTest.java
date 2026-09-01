package com.fincorex.investment;

import com.fincorex.investment.batch.InvestmentBatchItemProcessor;
import com.fincorex.investment.cache.InvestmentCacheManager;
import com.fincorex.investment.dto.InvestmentDTO;
import com.fincorex.investment.entity.InvestmentRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvestmentBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Investment & Portfolio Platform")
    void testBatchProcessor() throws Exception {
        InvestmentBatchItemProcessor processor = new InvestmentBatchItemProcessor();
        InvestmentRecordEntity entity = new InvestmentRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        InvestmentDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Investment & Portfolio Platform")
    void testCacheManager() {
        InvestmentCacheManager cache = new InvestmentCacheManager();
        InvestmentDTO dto = new InvestmentDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<InvestmentDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
