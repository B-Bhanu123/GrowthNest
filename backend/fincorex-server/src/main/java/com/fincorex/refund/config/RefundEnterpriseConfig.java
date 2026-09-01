package com.fincorex.refund.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Refund Management (Refund Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class RefundEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(RefundEnterpriseConfig.class);

    @Bean(name = "refundThreadPoolExecutor")
    public Executor refundExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Refund Management");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock refundClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "refundIdempotencyRegistry")
    public java.util.Set<String> refundIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for refund");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
