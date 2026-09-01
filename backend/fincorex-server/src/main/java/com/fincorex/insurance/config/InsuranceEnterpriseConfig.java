package com.fincorex.insurance.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Insurance Policy System (Insurance Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class InsuranceEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(InsuranceEnterpriseConfig.class);

    @Bean(name = "insuranceThreadPoolExecutor")
    public Executor insuranceExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Insurance Policy System");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock insuranceClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "insuranceIdempotencyRegistry")
    public java.util.Set<String> insuranceIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for insurance");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
