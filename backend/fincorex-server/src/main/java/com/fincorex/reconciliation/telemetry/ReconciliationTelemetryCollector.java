package com.fincorex.reconciliation.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Automated Bank Reconciliation (Reconciliation)
 */
@Component
public class ReconciliationTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public ReconciliationTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.reconciliation.operations.success")
                .description("Total successful operations in Automated Bank Reconciliation")
                .tag("module", "reconciliation")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.reconciliation.operations.failure")
                .description("Total failed operations in Automated Bank Reconciliation")
                .tag("module", "reconciliation")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.reconciliation.execution.time")
                .description("Latency distribution for Automated Bank Reconciliation execution")
                .tag("module", "reconciliation")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in reconciliation (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in reconciliation with error code: {}", errorCode);
    }
}
