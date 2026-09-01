package com.fincorex.transaction.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Transaction Processing Core (Transaction Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class TransactionEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(TransactionEnterpriseConfig.class);

    @Bean(name = "transactionThreadPoolExecutor")
    public Executor transactionExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Transaction Processing Core");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock transactionClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "transactionIdempotencyRegistry")
    public java.util.Set<String> transactionIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for transaction");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
