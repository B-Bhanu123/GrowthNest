package com.fincorex.accounting.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for General Accounting & Trial Balance (Accounting)
 */
@Component
public class AccountingTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(AccountingTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public AccountingTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.accounting.operations.success")
                .description("Total successful operations in General Accounting & Trial Balance")
                .tag("module", "accounting")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.accounting.operations.failure")
                .description("Total failed operations in General Accounting & Trial Balance")
                .tag("module", "accounting")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.accounting.execution.time")
                .description("Latency distribution for General Accounting & Trial Balance execution")
                .tag("module", "accounting")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in accounting (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in accounting with error code: {}", errorCode);
    }
}
