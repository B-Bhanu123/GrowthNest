package com.fincorex.settlement;

import com.fincorex.settlement.batch.SettlementBatchItemProcessor;
import com.fincorex.settlement.cache.SettlementCacheManager;
import com.fincorex.settlement.dto.SettlementDTO;
import com.fincorex.settlement.entity.SettlementRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SettlementBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Merchant Batch Settlement")
    void testBatchProcessor() throws Exception {
        SettlementBatchItemProcessor processor = new SettlementBatchItemProcessor();
        SettlementRecordEntity entity = new SettlementRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        SettlementDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Merchant Batch Settlement")
    void testCacheManager() {
        SettlementCacheManager cache = new SettlementCacheManager();
        SettlementDTO dto = new SettlementDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<SettlementDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
