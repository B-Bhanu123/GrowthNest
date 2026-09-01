package com.fincorex.dispute;

import com.fincorex.dispute.batch.DisputeBatchItemProcessor;
import com.fincorex.dispute.cache.DisputeCacheManager;
import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.entity.DisputeRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DisputeBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Dispute & Chargeback Handling")
    void testBatchProcessor() throws Exception {
        DisputeBatchItemProcessor processor = new DisputeBatchItemProcessor();
        DisputeRecordEntity entity = new DisputeRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        DisputeDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Dispute & Chargeback Handling")
    void testCacheManager() {
        DisputeCacheManager cache = new DisputeCacheManager();
        DisputeDTO dto = new DisputeDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<DisputeDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
