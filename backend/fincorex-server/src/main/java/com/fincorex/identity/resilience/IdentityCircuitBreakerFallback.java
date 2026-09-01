package com.fincorex.identity.resilience;

import com.fincorex.identity.dto.IdentityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Identity & Access Management
 */
@Component
public class IdentityCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(IdentityCircuitBreakerFallback.class);

    public IdentityDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Identity & Access Management ref: {} due to: {}", referenceCode, t.getMessage());

        IdentityDTO fallback = new IdentityDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public IdentityDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Identity & Access Management ref: {}", referenceCode, t);
        return new IdentityDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
