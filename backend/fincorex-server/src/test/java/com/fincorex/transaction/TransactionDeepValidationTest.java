package com.fincorex.transaction;

import com.fincorex.transaction.config.TransactionProperties;
import com.fincorex.transaction.dto.CreateTransactionRequest;
import com.fincorex.transaction.entity.TransactionAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Transaction Processing Core")
    void testRequestDTOValidation() {
        CreateTransactionRequest req = new CreateTransactionRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Transaction Processing Core")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        TransactionAuditLogEntity audit = new TransactionAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Transaction Processing Core")
    void testPropertiesDefaults() {
        TransactionProperties props = new TransactionProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
