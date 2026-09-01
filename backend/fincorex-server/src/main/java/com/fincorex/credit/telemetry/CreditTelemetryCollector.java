package com.fincorex.credit.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Credit Scoring System (Credit)
 */
@Component
public class CreditTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(CreditTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public CreditTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.credit.operations.success")
                .description("Total successful operations in Credit Scoring System")
                .tag("module", "credit")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.credit.operations.failure")
                .description("Total failed operations in Credit Scoring System")
                .tag("module", "credit")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.credit.execution.time")
                .description("Latency distribution for Credit Scoring System execution")
                .tag("module", "credit")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in credit (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in credit with error code: {}", errorCode);
    }
}
