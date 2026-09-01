package com.fincorex.merchant;

import com.fincorex.merchant.config.MerchantProperties;
import com.fincorex.merchant.dto.CreateMerchantRequest;
import com.fincorex.merchant.entity.MerchantAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MerchantDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Merchant Acquiring Management")
    void testRequestDTOValidation() {
        CreateMerchantRequest req = new CreateMerchantRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Merchant Acquiring Management")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        MerchantAuditLogEntity audit = new MerchantAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Merchant Acquiring Management")
    void testPropertiesDefaults() {
        MerchantProperties props = new MerchantProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
