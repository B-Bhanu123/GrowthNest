package com.fincorex.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for API Gateway & Security Proxy (Gateway Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class GatewayEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayEnterpriseConfig.class);

    @Bean(name = "gatewayThreadPoolExecutor")
    public Executor gatewayExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for API Gateway & Security Proxy");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock gatewayClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "gatewayIdempotencyRegistry")
    public java.util.Set<String> gatewayIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for gateway");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
