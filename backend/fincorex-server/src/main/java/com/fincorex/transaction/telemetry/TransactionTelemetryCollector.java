package com.fincorex.transaction.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Transaction Processing Core (Transaction)
 */
@Component
public class TransactionTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(TransactionTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public TransactionTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.transaction.operations.success")
                .description("Total successful operations in Transaction Processing Core")
                .tag("module", "transaction")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.transaction.operations.failure")
                .description("Total failed operations in Transaction Processing Core")
                .tag("module", "transaction")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.transaction.execution.time")
                .description("Latency distribution for Transaction Processing Core execution")
                .tag("module", "transaction")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in transaction (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in transaction with error code: {}", errorCode);
    }
}
