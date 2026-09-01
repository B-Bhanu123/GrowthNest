package com.fincorex.audit;

import com.fincorex.audit.batch.AuditBatchItemProcessor;
import com.fincorex.audit.cache.AuditCacheManager;
import com.fincorex.audit.dto.AuditDTO;
import com.fincorex.audit.entity.AuditRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuditBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Immutable Audit Logging")
    void testBatchProcessor() throws Exception {
        AuditBatchItemProcessor processor = new AuditBatchItemProcessor();
        AuditRecordEntity entity = new AuditRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        AuditDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Immutable Audit Logging")
    void testCacheManager() {
        AuditCacheManager cache = new AuditCacheManager();
        AuditDTO dto = new AuditDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<AuditDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
