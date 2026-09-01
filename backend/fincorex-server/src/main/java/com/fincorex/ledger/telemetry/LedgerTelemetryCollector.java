package com.fincorex.ledger.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Double-Entry Financial Ledger (Ledger)
 */
@Component
public class LedgerTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(LedgerTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public LedgerTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.ledger.operations.success")
                .description("Total successful operations in Double-Entry Financial Ledger")
                .tag("module", "ledger")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.ledger.operations.failure")
                .description("Total failed operations in Double-Entry Financial Ledger")
                .tag("module", "ledger")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.ledger.execution.time")
                .description("Latency distribution for Double-Entry Financial Ledger execution")
                .tag("module", "ledger")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in ledger (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in ledger with error code: {}", errorCode);
    }
}
