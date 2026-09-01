package com.fincorex.settlement.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Merchant Batch Settlement (Settlement)
 */
@Component
public class SettlementTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(SettlementTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public SettlementTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.settlement.operations.success")
                .description("Total successful operations in Merchant Batch Settlement")
                .tag("module", "settlement")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.settlement.operations.failure")
                .description("Total failed operations in Merchant Batch Settlement")
                .tag("module", "settlement")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.settlement.execution.time")
                .description("Latency distribution for Merchant Batch Settlement execution")
                .tag("module", "settlement")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in settlement (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in settlement with error code: {}", errorCode);
    }
}
