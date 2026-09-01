package com.fincorex.fraud;

import com.fincorex.fraud.batch.FraudBatchItemProcessor;
import com.fincorex.fraud.cache.FraudCacheManager;
import com.fincorex.fraud.dto.FraudDTO;
import com.fincorex.fraud.entity.FraudRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FraudBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Real-Time Fraud Detection Engine")
    void testBatchProcessor() throws Exception {
        FraudBatchItemProcessor processor = new FraudBatchItemProcessor();
        FraudRecordEntity entity = new FraudRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        FraudDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Real-Time Fraud Detection Engine")
    void testCacheManager() {
        FraudCacheManager cache = new FraudCacheManager();
        FraudDTO dto = new FraudDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<FraudDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
