package com.fincorex.insurance;

import com.fincorex.insurance.batch.InsuranceBatchItemProcessor;
import com.fincorex.insurance.cache.InsuranceCacheManager;
import com.fincorex.insurance.dto.InsuranceDTO;
import com.fincorex.insurance.entity.InsuranceRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Insurance Policy System")
    void testBatchProcessor() throws Exception {
        InsuranceBatchItemProcessor processor = new InsuranceBatchItemProcessor();
        InsuranceRecordEntity entity = new InsuranceRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        InsuranceDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Insurance Policy System")
    void testCacheManager() {
        InsuranceCacheManager cache = new InsuranceCacheManager();
        InsuranceDTO dto = new InsuranceDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<InsuranceDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
