package com.fincorex.lending.resilience;

import com.fincorex.lending.dto.LendingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Lending & Underwriting Engine
 */
@Component
public class LendingCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(LendingCircuitBreakerFallback.class);

    public LendingDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Lending & Underwriting Engine ref: {} due to: {}", referenceCode, t.getMessage());

        LendingDTO fallback = new LendingDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public LendingDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Lending & Underwriting Engine ref: {}", referenceCode, t);
        return new LendingDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
