package com.fincorex.fraud.resilience;

import com.fincorex.fraud.dto.FraudDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Real-Time Fraud Detection Engine
 */
@Component
public class FraudCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(FraudCircuitBreakerFallback.class);

    public FraudDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Real-Time Fraud Detection Engine ref: {} due to: {}", referenceCode, t.getMessage());

        FraudDTO fallback = new FraudDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public FraudDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Real-Time Fraud Detection Engine ref: {}", referenceCode, t);
        return new FraudDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
