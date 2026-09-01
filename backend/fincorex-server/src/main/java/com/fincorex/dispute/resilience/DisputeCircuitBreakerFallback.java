package com.fincorex.dispute.resilience;

import com.fincorex.dispute.dto.DisputeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Dispute & Chargeback Handling
 */
@Component
public class DisputeCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(DisputeCircuitBreakerFallback.class);

    public DisputeDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Dispute & Chargeback Handling ref: {} due to: {}", referenceCode, t.getMessage());

        DisputeDTO fallback = new DisputeDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public DisputeDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Dispute & Chargeback Handling ref: {}", referenceCode, t);
        return new DisputeDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
