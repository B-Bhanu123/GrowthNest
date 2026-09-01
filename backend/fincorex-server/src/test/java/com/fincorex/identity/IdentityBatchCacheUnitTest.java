package com.fincorex.identity;

import com.fincorex.identity.batch.IdentityBatchItemProcessor;
import com.fincorex.identity.cache.IdentityCacheManager;
import com.fincorex.identity.dto.IdentityDTO;
import com.fincorex.identity.entity.IdentityRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class IdentityBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Identity & Access Management")
    void testBatchProcessor() throws Exception {
        IdentityBatchItemProcessor processor = new IdentityBatchItemProcessor();
        IdentityRecordEntity entity = new IdentityRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        IdentityDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Identity & Access Management")
    void testCacheManager() {
        IdentityCacheManager cache = new IdentityCacheManager();
        IdentityDTO dto = new IdentityDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<IdentityDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
