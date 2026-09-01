package com.fincorex.lending;

import com.fincorex.lending.batch.LendingBatchItemProcessor;
import com.fincorex.lending.cache.LendingCacheManager;
import com.fincorex.lending.dto.LendingDTO;
import com.fincorex.lending.entity.LendingRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LendingBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Lending & Underwriting Engine")
    void testBatchProcessor() throws Exception {
        LendingBatchItemProcessor processor = new LendingBatchItemProcessor();
        LendingRecordEntity entity = new LendingRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        LendingDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Lending & Underwriting Engine")
    void testCacheManager() {
        LendingCacheManager cache = new LendingCacheManager();
        LendingDTO dto = new LendingDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<LendingDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
