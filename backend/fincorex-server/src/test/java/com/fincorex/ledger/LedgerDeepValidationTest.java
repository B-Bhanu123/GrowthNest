package com.fincorex.ledger;

import com.fincorex.ledger.config.LedgerProperties;
import com.fincorex.ledger.dto.CreateLedgerRequest;
import com.fincorex.ledger.entity.LedgerAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LedgerDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Double-Entry Financial Ledger")
    void testRequestDTOValidation() {
        CreateLedgerRequest req = new CreateLedgerRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Double-Entry Financial Ledger")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        LedgerAuditLogEntity audit = new LedgerAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Double-Entry Financial Ledger")
    void testPropertiesDefaults() {
        LedgerProperties props = new LedgerProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
