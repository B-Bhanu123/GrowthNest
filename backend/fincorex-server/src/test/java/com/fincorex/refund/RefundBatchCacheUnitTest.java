package com.fincorex.refund;

import com.fincorex.refund.batch.RefundBatchItemProcessor;
import com.fincorex.refund.cache.RefundCacheManager;
import com.fincorex.refund.dto.RefundDTO;
import com.fincorex.refund.entity.RefundRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RefundBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Refund Management")
    void testBatchProcessor() throws Exception {
        RefundBatchItemProcessor processor = new RefundBatchItemProcessor();
        RefundRecordEntity entity = new RefundRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        RefundDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Refund Management")
    void testCacheManager() {
        RefundCacheManager cache = new RefundCacheManager();
        RefundDTO dto = new RefundDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<RefundDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
