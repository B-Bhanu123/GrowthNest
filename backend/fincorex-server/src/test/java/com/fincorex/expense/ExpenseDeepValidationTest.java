package com.fincorex.expense;

import com.fincorex.expense.config.ExpenseProperties;
import com.fincorex.expense.dto.CreateExpenseRequest;
import com.fincorex.expense.entity.ExpenseAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExpenseDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Corporate Expense Management")
    void testRequestDTOValidation() {
        CreateExpenseRequest req = new CreateExpenseRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Corporate Expense Management")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        ExpenseAuditLogEntity audit = new ExpenseAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Corporate Expense Management")
    void testPropertiesDefaults() {
        ExpenseProperties props = new ExpenseProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
