package com.fincorex.reconciliation;

import com.fincorex.reconciliation.batch.ReconciliationBatchItemProcessor;
import com.fincorex.reconciliation.cache.ReconciliationCacheManager;
import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReconciliationBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Automated Bank Reconciliation")
    void testBatchProcessor() throws Exception {
        ReconciliationBatchItemProcessor processor = new ReconciliationBatchItemProcessor();
        ReconciliationRecordEntity entity = new ReconciliationRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        ReconciliationDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Automated Bank Reconciliation")
    void testCacheManager() {
        ReconciliationCacheManager cache = new ReconciliationCacheManager();
        ReconciliationDTO dto = new ReconciliationDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<ReconciliationDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
