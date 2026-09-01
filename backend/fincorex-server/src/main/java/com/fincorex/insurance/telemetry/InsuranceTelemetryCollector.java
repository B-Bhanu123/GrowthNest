package com.fincorex.insurance.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Insurance Policy System (Insurance)
 */
@Component
public class InsuranceTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(InsuranceTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public InsuranceTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.insurance.operations.success")
                .description("Total successful operations in Insurance Policy System")
                .tag("module", "insurance")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.insurance.operations.failure")
                .description("Total failed operations in Insurance Policy System")
                .tag("module", "insurance")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.insurance.execution.time")
                .description("Latency distribution for Insurance Policy System execution")
                .tag("module", "insurance")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in insurance (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in insurance with error code: {}", errorCode);
    }
}
