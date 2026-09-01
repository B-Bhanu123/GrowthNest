package com.fincorex.merchant;

import com.fincorex.merchant.batch.MerchantBatchItemProcessor;
import com.fincorex.merchant.cache.MerchantCacheManager;
import com.fincorex.merchant.dto.MerchantDTO;
import com.fincorex.merchant.entity.MerchantRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MerchantBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Merchant Acquiring Management")
    void testBatchProcessor() throws Exception {
        MerchantBatchItemProcessor processor = new MerchantBatchItemProcessor();
        MerchantRecordEntity entity = new MerchantRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        MerchantDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Merchant Acquiring Management")
    void testCacheManager() {
        MerchantCacheManager cache = new MerchantCacheManager();
        MerchantDTO dto = new MerchantDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<MerchantDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
