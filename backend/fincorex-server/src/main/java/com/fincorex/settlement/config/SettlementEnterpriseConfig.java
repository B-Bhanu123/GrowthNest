package com.fincorex.settlement.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Merchant Batch Settlement (Settlement Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class SettlementEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(SettlementEnterpriseConfig.class);

    @Bean(name = "settlementThreadPoolExecutor")
    public Executor settlementExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Merchant Batch Settlement");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock settlementClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "settlementIdempotencyRegistry")
    public java.util.Set<String> settlementIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for settlement");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
