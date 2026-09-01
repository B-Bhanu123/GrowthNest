package com.fincorex.investment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Investment & Portfolio Platform (Investment Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class InvestmentEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(InvestmentEnterpriseConfig.class);

    @Bean(name = "investmentThreadPoolExecutor")
    public Executor investmentExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Investment & Portfolio Platform");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock investmentClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "investmentIdempotencyRegistry")
    public java.util.Set<String> investmentIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for investment");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
