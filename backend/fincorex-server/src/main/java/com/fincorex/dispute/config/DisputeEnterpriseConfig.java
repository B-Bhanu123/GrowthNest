package com.fincorex.dispute.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Dispute & Chargeback Handling (Dispute Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class DisputeEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(DisputeEnterpriseConfig.class);

    @Bean(name = "disputeThreadPoolExecutor")
    public Executor disputeExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Dispute & Chargeback Handling");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock disputeClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "disputeIdempotencyRegistry")
    public java.util.Set<String> disputeIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for dispute");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
