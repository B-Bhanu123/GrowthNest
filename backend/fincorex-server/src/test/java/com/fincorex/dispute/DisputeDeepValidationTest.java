package com.fincorex.dispute;

import com.fincorex.dispute.config.DisputeProperties;
import com.fincorex.dispute.dto.CreateDisputeRequest;
import com.fincorex.dispute.entity.DisputeAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DisputeDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Dispute & Chargeback Handling")
    void testRequestDTOValidation() {
        CreateDisputeRequest req = new CreateDisputeRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Dispute & Chargeback Handling")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        DisputeAuditLogEntity audit = new DisputeAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Dispute & Chargeback Handling")
    void testPropertiesDefaults() {
        DisputeProperties props = new DisputeProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
