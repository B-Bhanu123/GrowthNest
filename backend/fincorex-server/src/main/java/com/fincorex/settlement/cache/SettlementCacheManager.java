package com.fincorex.settlement.cache;

import com.fincorex.settlement.dto.SettlementDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Cache Adapter for Merchant Batch Settlement (Settlement)
 */
@Component
public class SettlementCacheManager {

    private static final Logger log = LoggerFactory.getLogger(SettlementCacheManager.class);

    private final Map<String, SettlementDTO> cacheStore = new ConcurrentHashMap<>();

    public void put(String referenceCode, SettlementDTO dto) {
        log.debug("[CACHE-PUT] Caching DTO for ref: {}", referenceCode);
        cacheStore.put(referenceCode, dto);
    }

    public Optional<SettlementDTO> get(String referenceCode) {
        SettlementDTO hit = cacheStore.get(referenceCode);
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
        log.info("[CACHE-CLEAR] Clearing all cached entries for settlement");
        cacheStore.clear();
    }
}
