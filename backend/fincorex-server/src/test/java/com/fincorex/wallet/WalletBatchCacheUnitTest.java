package com.fincorex.wallet;

import com.fincorex.wallet.batch.WalletBatchItemProcessor;
import com.fincorex.wallet.cache.WalletCacheManager;
import com.fincorex.wallet.dto.WalletDTO;
import com.fincorex.wallet.entity.WalletRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class WalletBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Stored-Value Digital Wallet")
    void testBatchProcessor() throws Exception {
        WalletBatchItemProcessor processor = new WalletBatchItemProcessor();
        WalletRecordEntity entity = new WalletRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        WalletDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Stored-Value Digital Wallet")
    void testCacheManager() {
        WalletCacheManager cache = new WalletCacheManager();
        WalletDTO dto = new WalletDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<WalletDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
