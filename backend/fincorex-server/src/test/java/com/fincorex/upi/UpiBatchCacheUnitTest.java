package com.fincorex.upi;

import com.fincorex.upi.batch.UpiBatchItemProcessor;
import com.fincorex.upi.cache.UpiCacheManager;
import com.fincorex.upi.dto.UpiDTO;
import com.fincorex.upi.entity.UpiRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UpiBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for UPI Instant Transfer Network")
    void testBatchProcessor() throws Exception {
        UpiBatchItemProcessor processor = new UpiBatchItemProcessor();
        UpiRecordEntity entity = new UpiRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        UpiDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for UPI Instant Transfer Network")
    void testCacheManager() {
        UpiCacheManager cache = new UpiCacheManager();
        UpiDTO dto = new UpiDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<UpiDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
