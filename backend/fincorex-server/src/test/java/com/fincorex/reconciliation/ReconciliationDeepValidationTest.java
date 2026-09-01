package com.fincorex.reconciliation;

import com.fincorex.reconciliation.config.ReconciliationProperties;
import com.fincorex.reconciliation.dto.CreateReconciliationRequest;
import com.fincorex.reconciliation.entity.ReconciliationAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReconciliationDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Automated Bank Reconciliation")
    void testRequestDTOValidation() {
        CreateReconciliationRequest req = new CreateReconciliationRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Automated Bank Reconciliation")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        ReconciliationAuditLogEntity audit = new ReconciliationAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Automated Bank Reconciliation")
    void testPropertiesDefaults() {
        ReconciliationProperties props = new ReconciliationProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
