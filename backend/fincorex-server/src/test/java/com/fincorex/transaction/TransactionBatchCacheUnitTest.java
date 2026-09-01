package com.fincorex.transaction;

import com.fincorex.transaction.batch.TransactionBatchItemProcessor;
import com.fincorex.transaction.cache.TransactionCacheManager;
import com.fincorex.transaction.dto.TransactionDTO;
import com.fincorex.transaction.entity.TransactionRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Transaction Processing Core")
    void testBatchProcessor() throws Exception {
        TransactionBatchItemProcessor processor = new TransactionBatchItemProcessor();
        TransactionRecordEntity entity = new TransactionRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        TransactionDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Transaction Processing Core")
    void testCacheManager() {
        TransactionCacheManager cache = new TransactionCacheManager();
        TransactionDTO dto = new TransactionDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<TransactionDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
