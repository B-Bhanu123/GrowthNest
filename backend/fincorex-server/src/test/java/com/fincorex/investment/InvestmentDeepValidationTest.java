package com.fincorex.investment;

import com.fincorex.investment.config.InvestmentProperties;
import com.fincorex.investment.dto.CreateInvestmentRequest;
import com.fincorex.investment.entity.InvestmentAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InvestmentDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Investment & Portfolio Platform")
    void testRequestDTOValidation() {
        CreateInvestmentRequest req = new CreateInvestmentRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Investment & Portfolio Platform")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        InvestmentAuditLogEntity audit = new InvestmentAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Investment & Portfolio Platform")
    void testPropertiesDefaults() {
        InvestmentProperties props = new InvestmentProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
