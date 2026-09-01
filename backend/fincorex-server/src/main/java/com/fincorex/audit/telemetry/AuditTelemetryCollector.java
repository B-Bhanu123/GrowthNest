package com.fincorex.audit.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Immutable Audit Logging (Audit)
 */
@Component
public class AuditTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(AuditTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public AuditTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.audit.operations.success")
                .description("Total successful operations in Immutable Audit Logging")
                .tag("module", "audit")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.audit.operations.failure")
                .description("Total failed operations in Immutable Audit Logging")
                .tag("module", "audit")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.audit.execution.time")
                .description("Latency distribution for Immutable Audit Logging execution")
                .tag("module", "audit")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in audit (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in audit with error code: {}", errorCode);
    }
}
