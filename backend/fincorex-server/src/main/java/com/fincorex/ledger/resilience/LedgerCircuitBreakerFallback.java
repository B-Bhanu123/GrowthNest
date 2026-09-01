package com.fincorex.ledger.resilience;

import com.fincorex.ledger.dto.LedgerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Double-Entry Financial Ledger
 */
@Component
public class LedgerCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(LedgerCircuitBreakerFallback.class);

    public LedgerDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Double-Entry Financial Ledger ref: {} due to: {}", referenceCode, t.getMessage());

        LedgerDTO fallback = new LedgerDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public LedgerDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Double-Entry Financial Ledger ref: {}", referenceCode, t);
        return new LedgerDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
