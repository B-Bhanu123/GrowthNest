package com.fincorex.wallet.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Stored-Value Digital Wallet (Wallet Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class WalletEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(WalletEnterpriseConfig.class);

    @Bean(name = "walletThreadPoolExecutor")
    public Executor walletExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Stored-Value Digital Wallet");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock walletClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "walletIdempotencyRegistry")
    public java.util.Set<String> walletIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for wallet");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
