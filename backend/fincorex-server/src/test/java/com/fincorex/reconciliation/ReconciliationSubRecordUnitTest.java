package com.fincorex.reconciliation;

import com.fincorex.reconciliation.entity.ReconciliationSubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ReconciliationSubRecordUnitTest {

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for Automated Bank Reconciliation")
    void testSubRecordEntityCreation() {
        UUID parentId = UUID.randomUUID();
        ReconciliationSubRecordEntity sub = new ReconciliationSubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }
}
