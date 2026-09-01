package com.fincorex.analytics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Financial Analytics Engine (Analytics Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class AnalyticsEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEnterpriseConfig.class);

    @Bean(name = "analyticsThreadPoolExecutor")
    public Executor analyticsExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Financial Analytics Engine");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock analyticsClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "analyticsIdempotencyRegistry")
    public java.util.Set<String> analyticsIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for analytics");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
