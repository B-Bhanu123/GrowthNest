package com.fincorex.investment.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Investment & Portfolio Platform (Investment)
 */
@Component
public class InvestmentTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(InvestmentTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public InvestmentTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.investment.operations.success")
                .description("Total successful operations in Investment & Portfolio Platform")
                .tag("module", "investment")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.investment.operations.failure")
                .description("Total failed operations in Investment & Portfolio Platform")
                .tag("module", "investment")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.investment.execution.time")
                .description("Latency distribution for Investment & Portfolio Platform execution")
                .tag("module", "investment")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in investment (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in investment with error code: {}", errorCode);
    }
}
