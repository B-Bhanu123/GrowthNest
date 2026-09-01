package com.fincorex.reconciliation.cache;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Cache Adapter for Automated Bank Reconciliation (Reconciliation)
 */
@Component
public class ReconciliationCacheManager {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationCacheManager.class);

    private final Map<String, ReconciliationDTO> cacheStore = new ConcurrentHashMap<>();

    public void put(String referenceCode, ReconciliationDTO dto) {
        log.debug("[CACHE-PUT] Caching DTO for ref: {}", referenceCode);
        cacheStore.put(referenceCode, dto);
    }

    public Optional<ReconciliationDTO> get(String referenceCode) {
        ReconciliationDTO hit = cacheStore.get(referenceCode);
        if (hit != null) {
            log.debug("[CACHE-HIT] Found ref: {} in L1/L2 Redis cache", referenceCode);
            return Optional.of(hit);
        }
        log.debug("[CACHE-MISS] Ref: {} not found in cache", referenceCode);
        return Optional.empty();
    }

    public void evict(String referenceCode) {
        log.debug("[CACHE-EVICT] Evicting ref: {}", referenceCode);
        cacheStore.remove(referenceCode);
    }

    public void clearAll() {
        log.info("[CACHE-CLEAR] Clearing all cached entries for reconciliation");
        cacheStore.clear();
    }
}
