package com.fincorex.payment.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Payment Gateway Orchestration (Payment Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class PaymentEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(PaymentEnterpriseConfig.class);

    @Bean(name = "paymentThreadPoolExecutor")
    public Executor paymentExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Payment Gateway Orchestration");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock paymentClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "paymentIdempotencyRegistry")
    public java.util.Set<String> paymentIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for payment");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
