package com.fincorex.analytics;

import com.fincorex.analytics.batch.AnalyticsBatchItemProcessor;
import com.fincorex.analytics.cache.AnalyticsCacheManager;
import com.fincorex.analytics.dto.AnalyticsDTO;
import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Financial Analytics Engine")
    void testBatchProcessor() throws Exception {
        AnalyticsBatchItemProcessor processor = new AnalyticsBatchItemProcessor();
        AnalyticsRecordEntity entity = new AnalyticsRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        AnalyticsDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Financial Analytics Engine")
    void testCacheManager() {
        AnalyticsCacheManager cache = new AnalyticsCacheManager();
        AnalyticsDTO dto = new AnalyticsDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<AnalyticsDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
