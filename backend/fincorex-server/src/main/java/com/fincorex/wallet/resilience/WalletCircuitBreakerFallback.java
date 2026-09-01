package com.fincorex.wallet.resilience;

import com.fincorex.wallet.dto.WalletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Stored-Value Digital Wallet
 */
@Component
public class WalletCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(WalletCircuitBreakerFallback.class);

    public WalletDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Stored-Value Digital Wallet ref: {} due to: {}", referenceCode, t.getMessage());

        WalletDTO fallback = new WalletDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public WalletDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Stored-Value Digital Wallet ref: {}", referenceCode, t);
        return new WalletDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
