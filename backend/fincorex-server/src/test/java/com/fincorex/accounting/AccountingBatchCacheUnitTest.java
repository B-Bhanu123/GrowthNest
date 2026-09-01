package com.fincorex.accounting;

import com.fincorex.accounting.batch.AccountingBatchItemProcessor;
import com.fincorex.accounting.cache.AccountingCacheManager;
import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.entity.AccountingRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AccountingBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for General Accounting & Trial Balance")
    void testBatchProcessor() throws Exception {
        AccountingBatchItemProcessor processor = new AccountingBatchItemProcessor();
        AccountingRecordEntity entity = new AccountingRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        AccountingDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for General Accounting & Trial Balance")
    void testCacheManager() {
        AccountingCacheManager cache = new AccountingCacheManager();
        AccountingDTO dto = new AccountingDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<AccountingDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
