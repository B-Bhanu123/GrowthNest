package com.fincorex.admin;

import com.fincorex.admin.batch.AdminBatchItemProcessor;
import com.fincorex.admin.cache.AdminCacheManager;
import com.fincorex.admin.dto.AdminDTO;
import com.fincorex.admin.entity.AdminRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AdminBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Admin & Operations Center")
    void testBatchProcessor() throws Exception {
        AdminBatchItemProcessor processor = new AdminBatchItemProcessor();
        AdminRecordEntity entity = new AdminRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        AdminDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Admin & Operations Center")
    void testCacheManager() {
        AdminCacheManager cache = new AdminCacheManager();
        AdminDTO dto = new AdminDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<AdminDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
