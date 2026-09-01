package com.fincorex.notification;

import com.fincorex.notification.batch.NotificationBatchItemProcessor;
import com.fincorex.notification.cache.NotificationCacheManager;
import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.entity.NotificationRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for Centralized Notification System")
    void testBatchProcessor() throws Exception {
        NotificationBatchItemProcessor processor = new NotificationBatchItemProcessor();
        NotificationRecordEntity entity = new NotificationRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        NotificationDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for Centralized Notification System")
    void testCacheManager() {
        NotificationCacheManager cache = new NotificationCacheManager();
        NotificationDTO dto = new NotificationDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<NotificationDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
