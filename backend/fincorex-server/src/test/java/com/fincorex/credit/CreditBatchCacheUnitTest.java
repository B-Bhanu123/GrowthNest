package com.fincorex.credit;

import com.fincorex.credit.batch.CreditBatchItemProcessor;
import com.fincorex.credit.cache.CreditCacheManager;
import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.entity.CreditRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreditBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Credit Scoring System")
    void testBatchProcessor() throws Exception {
        CreditBatchItemProcessor processor = new CreditBatchItemProcessor();
        CreditRecordEntity entity = new CreditRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        CreditDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Credit Scoring System")
    void testCacheManager() {
        CreditCacheManager cache = new CreditCacheManager();
        CreditDTO dto = new CreditDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<CreditDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
