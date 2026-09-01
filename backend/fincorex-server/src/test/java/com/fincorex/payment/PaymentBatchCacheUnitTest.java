package com.fincorex.payment;

import com.fincorex.payment.batch.PaymentBatchItemProcessor;
import com.fincorex.payment.cache.PaymentCacheManager;
import com.fincorex.payment.dto.PaymentDTO;
import com.fincorex.payment.entity.PaymentRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Payment Gateway Orchestration")
    void testBatchProcessor() throws Exception {
        PaymentBatchItemProcessor processor = new PaymentBatchItemProcessor();
        PaymentRecordEntity entity = new PaymentRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        PaymentDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Payment Gateway Orchestration")
    void testCacheManager() {
        PaymentCacheManager cache = new PaymentCacheManager();
        PaymentDTO dto = new PaymentDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<PaymentDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
