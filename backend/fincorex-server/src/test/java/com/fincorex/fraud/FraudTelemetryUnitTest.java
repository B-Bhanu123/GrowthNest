package com.fincorex.fraud;

import com.fincorex.fraud.dto.FraudSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FraudTelemetryUnitTest {

    @Test
    @DisplayName("Verify Real-Time Fraud Detection Engine SearchCriteria default paging and sorting parameters")
    void testSearchCriteriaDefaults() {
        FraudSearchCriteria criteria = new FraudSearchCriteria();
        assertEquals(0, criteria.getPage());
        assertEquals(20, criteria.getSize());
        assertEquals("createdAt", criteria.getSortBy());
        assertEquals("DESC", criteria.getSortDirection());
    }

    @Test
    @DisplayName("Verify Real-Time Fraud Detection Engine SearchCriteria range filters")
    void testSearchCriteriaFilters() {
        FraudSearchCriteria criteria = new FraudSearchCriteria();
        UUID owner = UUID.randomUUID();
        criteria.setOwnerId(owner);
        criteria.setMinAmount(new BigDecimal("100.00"));
        criteria.setMaxAmount(new BigDecimal("5000.00"));
        criteria.setStatuses(List.of("ACTIVE", "COMPLETED"));

        assertEquals(owner, criteria.getOwnerId());
        assertEquals(new BigDecimal("100.00"), criteria.getMinAmount());
        assertEquals(2, criteria.getStatuses().size());
    }
}
