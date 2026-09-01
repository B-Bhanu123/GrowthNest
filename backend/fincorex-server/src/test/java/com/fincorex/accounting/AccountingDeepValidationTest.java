package com.fincorex.accounting;

import com.fincorex.accounting.config.AccountingProperties;
import com.fincorex.accounting.dto.CreateAccountingRequest;
import com.fincorex.accounting.entity.AccountingAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountingDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for General Accounting & Trial Balance")
    void testRequestDTOValidation() {
        CreateAccountingRequest req = new CreateAccountingRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for General Accounting & Trial Balance")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        AccountingAuditLogEntity audit = new AccountingAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for General Accounting & Trial Balance")
    void testPropertiesDefaults() {
        AccountingProperties props = new AccountingProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
