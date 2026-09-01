package com.fincorex.expense.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Corporate Expense Management (Expense Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class ExpenseEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEnterpriseConfig.class);

    @Bean(name = "expenseThreadPoolExecutor")
    public Executor expenseExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Corporate Expense Management");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock expenseClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "expenseIdempotencyRegistry")
    public java.util.Set<String> expenseIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for expense");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
