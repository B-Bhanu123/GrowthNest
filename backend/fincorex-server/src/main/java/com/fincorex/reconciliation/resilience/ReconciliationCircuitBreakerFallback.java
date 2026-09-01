package com.fincorex.reconciliation.resilience;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Automated Bank Reconciliation
 */
@Component
public class ReconciliationCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationCircuitBreakerFallback.class);

    public ReconciliationDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Automated Bank Reconciliation ref: {} due to: {}", referenceCode, t.getMessage());

        ReconciliationDTO fallback = new ReconciliationDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public ReconciliationDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Automated Bank Reconciliation ref: {}", referenceCode, t);
        return new ReconciliationDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
