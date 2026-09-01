package com.fincorex.expense;

import com.fincorex.expense.batch.ExpenseBatchItemProcessor;
import com.fincorex.expense.cache.ExpenseCacheManager;
import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.entity.ExpenseRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Corporate Expense Management")
    void testBatchProcessor() throws Exception {
        ExpenseBatchItemProcessor processor = new ExpenseBatchItemProcessor();
        ExpenseRecordEntity entity = new ExpenseRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        ExpenseDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Corporate Expense Management")
    void testCacheManager() {
        ExpenseCacheManager cache = new ExpenseCacheManager();
        ExpenseDTO dto = new ExpenseDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<ExpenseDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
