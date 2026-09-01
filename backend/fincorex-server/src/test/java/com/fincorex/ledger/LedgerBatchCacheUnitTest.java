package com.fincorex.ledger;

import com.fincorex.ledger.batch.LedgerBatchItemProcessor;
import com.fincorex.ledger.cache.LedgerCacheManager;
import com.fincorex.ledger.dto.LedgerDTO;
import com.fincorex.ledger.entity.LedgerRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LedgerBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Double-Entry Financial Ledger")
    void testBatchProcessor() throws Exception {
        LedgerBatchItemProcessor processor = new LedgerBatchItemProcessor();
        LedgerRecordEntity entity = new LedgerRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        LedgerDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Double-Entry Financial Ledger")
    void testCacheManager() {
        LedgerCacheManager cache = new LedgerCacheManager();
        LedgerDTO dto = new LedgerDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<LedgerDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
