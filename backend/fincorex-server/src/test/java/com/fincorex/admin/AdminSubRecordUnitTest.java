package com.fincorex.admin;

import com.fincorex.admin.entity.AdminSubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AdminSubRecordUnitTest {

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for Admin & Operations Center")
    void testSubRecordEntityCreation() {
        UUID parentId = UUID.randomUUID();
        AdminSubRecordEntity sub = new AdminSubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }
}
