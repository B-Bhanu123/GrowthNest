package com.fincorex.customer;

import com.fincorex.customer.batch.CustomerBatchItemProcessor;
import com.fincorex.customer.cache.CustomerCacheManager;
import com.fincorex.customer.dto.CustomerDTO;
import com.fincorex.customer.entity.CustomerRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Customer & Account Management")
    void testBatchProcessor() throws Exception {
        CustomerBatchItemProcessor processor = new CustomerBatchItemProcessor();
        CustomerRecordEntity entity = new CustomerRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        CustomerDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Customer & Account Management")
    void testCacheManager() {
        CustomerCacheManager cache = new CustomerCacheManager();
        CustomerDTO dto = new CustomerDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<CustomerDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
