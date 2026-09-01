package com.fincorex.investment;

import com.fincorex.investment.dto.InvestmentSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InvestmentTelemetryUnitTest {

    @Test
    @DisplayName("Verify Investment & Portfolio Platform SearchCriteria default paging and sorting parameters")
    void testSearchCriteriaDefaults() {
        InvestmentSearchCriteria criteria = new InvestmentSearchCriteria();
        assertEquals(0, criteria.getPage());
        assertEquals(20, criteria.getSize());
        assertEquals("createdAt", criteria.getSortBy());
        assertEquals("DESC", criteria.getSortDirection());
    }

    @Test
    @DisplayName("Verify Investment & Portfolio Platform SearchCriteria range filters")
    void testSearchCriteriaFilters() {
        InvestmentSearchCriteria criteria = new InvestmentSearchCriteria();
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
