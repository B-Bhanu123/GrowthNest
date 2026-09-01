package com.fincorex.payment.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Payment Gateway Orchestration (Payment)
 */
@Component
public class PaymentTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(PaymentTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public PaymentTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.payment.operations.success")
                .description("Total successful operations in Payment Gateway Orchestration")
                .tag("module", "payment")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.payment.operations.failure")
                .description("Total failed operations in Payment Gateway Orchestration")
                .tag("module", "payment")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.payment.execution.time")
                .description("Latency distribution for Payment Gateway Orchestration execution")
                .tag("module", "payment")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in payment (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in payment with error code: {}", errorCode);
    }
}
