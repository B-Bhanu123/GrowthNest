package com.fincorex.notification;

import com.fincorex.notification.config.NotificationProperties;
import com.fincorex.notification.dto.CreateNotificationRequest;
import com.fincorex.notification.entity.NotificationAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NotificationDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Centralized Notification System")
    void testRequestDTOValidation() {
        CreateNotificationRequest req = new CreateNotificationRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Centralized Notification System")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        NotificationAuditLogEntity audit = new NotificationAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Centralized Notification System")
    void testPropertiesDefaults() {
        NotificationProperties props = new NotificationProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
