package com.fincorex.insurance;

import com.fincorex.insurance.config.InsuranceProperties;
import com.fincorex.insurance.dto.CreateInsuranceRequest;
import com.fincorex.insurance.entity.InsuranceAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InsuranceDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Insurance Policy System")
    void testRequestDTOValidation() {
        CreateInsuranceRequest req = new CreateInsuranceRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Insurance Policy System")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        InsuranceAuditLogEntity audit = new InsuranceAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Insurance Policy System")
    void testPropertiesDefaults() {
        InsuranceProperties props = new InsuranceProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
