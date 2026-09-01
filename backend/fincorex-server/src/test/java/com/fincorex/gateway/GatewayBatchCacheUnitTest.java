package com.fincorex.gateway;

import com.fincorex.gateway.batch.GatewayBatchItemProcessor;
import com.fincorex.gateway.cache.GatewayCacheManager;
import com.fincorex.gateway.dto.GatewayDTO;
import com.fincorex.gateway.entity.GatewayRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GatewayBatchCacheUnitTest {

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for API Gateway & Security Proxy")
    void testBatchProcessor() throws Exception {
        GatewayBatchItemProcessor processor = new GatewayBatchItemProcessor();
        GatewayRecordEntity entity = new GatewayRecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        GatewayDTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for API Gateway & Security Proxy")
    void testCacheManager() {
        GatewayCacheManager cache = new GatewayCacheManager();
        GatewayDTO dto = new GatewayDTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<GatewayDTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }
}
