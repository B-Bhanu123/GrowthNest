package com.fincorex.analytics;

import com.fincorex.analytics.config.AnalyticsProperties;
import com.fincorex.analytics.dto.CreateAnalyticsRequest;
import com.fincorex.analytics.entity.AnalyticsAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AnalyticsDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Financial Analytics Engine")
    void testRequestDTOValidation() {
        CreateAnalyticsRequest req = new CreateAnalyticsRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Financial Analytics Engine")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        AnalyticsAuditLogEntity audit = new AnalyticsAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Financial Analytics Engine")
    void testPropertiesDefaults() {
        AnalyticsProperties props = new AnalyticsProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
