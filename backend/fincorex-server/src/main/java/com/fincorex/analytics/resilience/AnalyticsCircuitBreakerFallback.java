package com.fincorex.analytics.resilience;

import com.fincorex.analytics.dto.AnalyticsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Financial Analytics Engine
 */
@Component
public class AnalyticsCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsCircuitBreakerFallback.class);

    public AnalyticsDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Financial Analytics Engine ref: {} due to: {}", referenceCode, t.getMessage());

        AnalyticsDTO fallback = new AnalyticsDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public AnalyticsDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Financial Analytics Engine ref: {}", referenceCode, t);
        return new AnalyticsDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
