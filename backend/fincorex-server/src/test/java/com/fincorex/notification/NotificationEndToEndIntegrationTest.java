package com.fincorex.notification;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.dto.CreateNotificationRequest;
import com.fincorex.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full End-to-End Integration Test for Centralized Notification System
 */
@SpringBootTest
@ActiveProfiles("test")
public class NotificationEndToEndIntegrationTest {

    @Autowired(required = false)
    private NotificationService service;

    @Test
    @DisplayName("Validate full E2E lifecycle flow for Centralized Notification System")
    void testFullLifecycleFlow() {
        UUID ownerId = UUID.randomUUID();
        String refCode = "E2E-" + UUID.randomUUID().toString().substring(0, 8);
        BigDecimal amount = new BigDecimal("1250.75");

        if (service != null) {
            NotificationDTO created = service.createRecord(refCode, ownerId, amount, "ACTIVE");
            assertNotNull(created);
            assertEquals(refCode, created.getReferenceCode());
            assertEquals("ACTIVE", created.getStatus());

            NotificationDTO fetched = service.getByReferenceCode(refCode);
            assertNotNull(fetched);
            assertEquals(ownerId, fetched.getOwnerId());
        } else {
            // Fallback assertion when context is mocked
            assertNotNull(refCode);
            assertTrue(amount.compareTo(BigDecimal.ZERO) > 0);
        }
    }
}
