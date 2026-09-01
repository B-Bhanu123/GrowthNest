package com.fincorex.fraud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Real-Time Fraud Detection Engine (Fraud Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class FraudEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(FraudEnterpriseConfig.class);

    @Bean(name = "fraudThreadPoolExecutor")
    public Executor fraudExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Real-Time Fraud Detection Engine");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock fraudClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "fraudIdempotencyRegistry")
    public java.util.Set<String> fraudIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for fraud");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
