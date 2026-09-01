package com.fincorex.insurance.resilience;

import com.fincorex.insurance.dto.InsuranceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Insurance Policy System
 */
@Component
public class InsuranceCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(InsuranceCircuitBreakerFallback.class);

    public InsuranceDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Insurance Policy System ref: {} due to: {}", referenceCode, t.getMessage());

        InsuranceDTO fallback = new InsuranceDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public InsuranceDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Insurance Policy System ref: {}", referenceCode, t);
        return new InsuranceDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
