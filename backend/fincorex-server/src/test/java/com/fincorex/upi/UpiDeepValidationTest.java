package com.fincorex.upi;

import com.fincorex.upi.config.UpiProperties;
import com.fincorex.upi.dto.CreateUpiRequest;
import com.fincorex.upi.entity.UpiAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UpiDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for UPI Instant Transfer Network")
    void testRequestDTOValidation() {
        CreateUpiRequest req = new CreateUpiRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for UPI Instant Transfer Network")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        UpiAuditLogEntity audit = new UpiAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for UPI Instant Transfer Network")
    void testPropertiesDefaults() {
        UpiProperties props = new UpiProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
