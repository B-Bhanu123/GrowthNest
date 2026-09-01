package com.fincorex.settlement.resilience;

import com.fincorex.settlement.dto.SettlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Merchant Batch Settlement
 */
@Component
public class SettlementCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(SettlementCircuitBreakerFallback.class);

    public SettlementDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Merchant Batch Settlement ref: {} due to: {}", referenceCode, t.getMessage());

        SettlementDTO fallback = new SettlementDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public SettlementDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Merchant Batch Settlement ref: {}", referenceCode, t);
        return new SettlementDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
