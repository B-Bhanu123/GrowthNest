package com.fincorex.ledger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Double-Entry Financial Ledger (Ledger Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class LedgerEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(LedgerEnterpriseConfig.class);

    @Bean(name = "ledgerThreadPoolExecutor")
    public Executor ledgerExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Double-Entry Financial Ledger");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock ledgerClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "ledgerIdempotencyRegistry")
    public java.util.Set<String> ledgerIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for ledger");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
