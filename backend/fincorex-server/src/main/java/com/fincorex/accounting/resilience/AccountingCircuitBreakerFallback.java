package com.fincorex.accounting.resilience;

import com.fincorex.accounting.dto.AccountingDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for General Accounting & Trial Balance
 */
@Component
public class AccountingCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(AccountingCircuitBreakerFallback.class);

    public AccountingDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for General Accounting & Trial Balance ref: {} due to: {}", referenceCode, t.getMessage());

        AccountingDTO fallback = new AccountingDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public AccountingDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for General Accounting & Trial Balance ref: {}", referenceCode, t);
        return new AccountingDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
