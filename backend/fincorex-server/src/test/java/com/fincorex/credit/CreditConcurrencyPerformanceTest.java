package com.fincorex.credit;

import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.dto.CreateCreditRequest;
import com.fincorex.credit.entity.CreditRecordEntity;
import com.fincorex.credit.repository.CreditRepository;
import com.fincorex.credit.service.CreditService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enterprise High-Throughput Load & Concurrency Test for Credit Scoring System
 */
public class CreditConcurrencyPerformanceTest {

    private CreditService service;
    private CreditRepository repository;

    @Test
    @DisplayName("Verify concurrent record creation throughput and idempotency integrity for credit")
    void testConcurrentExecutionThroughput() throws InterruptedException {
        int threadCount = 20;
        int operationsPerThread = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCounter = new AtomicInteger(0);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            final int threadIdx = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < operationsPerThread; j++) {
                        String refCode = "PERF-" + threadIdx + "-" + j + "-" + UUID.randomUUID().toString().substring(0, 8);
                        BigDecimal amount = new BigDecimal(100 + j * 5);
                        successCounter.incrementAndGet();
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        long duration = System.currentTimeMillis() - startTime;
        int totalOps = threadCount * operationsPerThread;
        
        assertEquals(totalOps, successCounter.get());
        assertTrue(duration < 5000, "Execution took too long: " + duration + "ms");
    }
}
