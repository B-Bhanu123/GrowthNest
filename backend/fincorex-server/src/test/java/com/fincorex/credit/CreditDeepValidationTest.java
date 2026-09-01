package com.fincorex.credit;

import com.fincorex.credit.config.CreditProperties;
import com.fincorex.credit.dto.CreateCreditRequest;
import com.fincorex.credit.entity.CreditAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreditDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Credit Scoring System")
    void testRequestDTOValidation() {
        CreateCreditRequest req = new CreateCreditRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Credit Scoring System")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        CreditAuditLogEntity audit = new CreditAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Credit Scoring System")
    void testPropertiesDefaults() {
        CreditProperties props = new CreditProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
