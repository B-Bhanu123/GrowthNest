package com.fincorex.expense.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Enterprise Micrometer Metrics & Telemetry Collector for Corporate Expense Management (Expense)
 */
@Component
public class ExpenseTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(ExpenseTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public ExpenseTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.expense.operations.success")
                .description("Total successful operations in Corporate Expense Management")
                .tag("module", "expense")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.expense.operations.failure")
                .description("Total failed operations in Corporate Expense Management")
                .tag("module", "expense")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.expense.execution.time")
                .description("Latency distribution for Corporate Expense Management execution")
                .tag("module", "expense")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in expense (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in expense with error code: {}", errorCode);
    }
}
