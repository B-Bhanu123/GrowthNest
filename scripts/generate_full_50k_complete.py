import os

BASE_JAVA = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "main", "java", "com", "fincorex")
BASE_TEST = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "test", "java", "com", "fincorex")
BASE_TS = os.path.join(os.getcwd(), "backend", "enterprise-services")

MODULES = [
    ("identity", "Identity & Access Management"),
    ("customer", "Customer & Account Management"),
    ("merchant", "Merchant Acquiring Management"),
    ("payment", "Payment Gateway Orchestration"),
    ("wallet", "Stored-Value Digital Wallet"),
    ("upi", "UPI Instant Transfer Network"),
    ("transaction", "Transaction Processing Core"),
    ("ledger", "Double-Entry Financial Ledger"),
    ("settlement", "Merchant Batch Settlement"),
    ("reconciliation", "Automated Bank Reconciliation"),
    ("refund", "Refund Management"),
    ("dispute", "Dispute & Chargeback Handling"),
    ("lending", "Lending & Underwriting Engine"),
    ("credit", "Credit Scoring System"),
    ("investment", "Investment & Portfolio Platform"),
    ("insurance", "Insurance Policy System"),
    ("fraud", "Real-Time Fraud Detection Engine"),
    ("accounting", "General Accounting & Trial Balance"),
    ("expense", "Corporate Expense Management"),
    ("analytics", "Financial Analytics Engine"),
    ("notification", "Centralized Notification System"),
    ("audit", "Immutable Audit Logging"),
    ("admin", "Admin & Operations Center"),
    ("gateway", "API Gateway & Security Proxy")
]

def generate_submodule_classes(mod_name, mod_title):
    cap = mod_name.capitalize()
    mod_upper = mod_name.upper()

    # 1. Secondary Entity Class for Advanced Relationships
    sec_entity = f"""package com.fincorex.{mod_name}.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Enterprise Sub-Domain Relationship Entity for {mod_title} ({cap})
 */
@Entity
@Table(name = "{mod_name}_sub_records", schema = "{mod_name}")
public class {cap}SubRecordEntity {{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID subRecordId;

    @Column(name = "parent_record_id", nullable = false)
    private UUID parentRecordId;

    @Column(name = "sub_type", nullable = false, length = 100)
    private String subType;

    @Column(name = "sub_value", precision = 18, scale = 4)
    private BigDecimal subValue;

    @Column(name = "status_flag", nullable = false, length = 50)
    private String statusFlag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public {cap}SubRecordEntity() {{
        this.createdAt = LocalDateTime.now();
    }}

    public {cap}SubRecordEntity(UUID parentRecordId, String subType, BigDecimal subValue, String statusFlag) {{
        this.parentRecordId = parentRecordId;
        this.subType = subType;
        this.subValue = subValue;
        this.statusFlag = statusFlag;
        this.createdAt = LocalDateTime.now();
    }}

    public UUID getSubRecordId() {{ return subRecordId; }}
    public void setSubRecordId(UUID subRecordId) {{ this.subRecordId = subRecordId; }}

    public UUID getParentRecordId() {{ return parentRecordId; }}
    public void setParentRecordId(UUID parentRecordId) {{ this.parentRecordId = parentRecordId; }}

    public String getSubType() {{ return subType; }}
    public void setSubType(String subType) {{ this.subType = subType; }}

    public BigDecimal getSubValue() {{ return subValue; }}
    public void setSubValue(BigDecimal subValue) {{ this.subValue = subValue; }}

    public String getStatusFlag() {{ return statusFlag; }}
    public void setStatusFlag(String statusFlag) {{ this.statusFlag = statusFlag; }}

    public LocalDateTime getCreatedAt() {{ return createdAt; }}
    public void setCreatedAt(LocalDateTime createdAt) {{ this.createdAt = createdAt; }}
}}
"""

    # 2. Secondary Repository
    sec_repo = f"""package com.fincorex.{mod_name}.repository;

import com.fincorex.{mod_name}.entity.{cap}SubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface {cap}SubRecordRepository extends JpaRepository<{cap}SubRecordEntity, UUID> {{
    List<{cap}SubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<{cap}SubRecordEntity> findBySubType(String subType);
}}
"""

    # 3. Data Mapper / Converter Class
    mapper_cls = f"""package com.fincorex.{mod_name}.mapper;

import com.fincorex.{mod_name}.dto.{cap}DTO;
import com.fincorex.{mod_name}.entity.{cap}RecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for {mod_title}
 */
@Component
public class {cap}DataMapper {{

    public {cap}DTO toDTO({cap}RecordEntity entity) {{
        if (entity == null) return null;
        {cap}DTO dto = new {cap}DTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }}

    public {cap}RecordEntity toEntity({cap}DTO dto) {{
        if (dto == null) return null;
        {cap}RecordEntity entity = new {cap}RecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }}
}}
"""

    # 4. Secondary Test Suite for SubRecord Operations
    sub_test = f"""package com.fincorex.{mod_name};

import com.fincorex.{mod_name}.entity.{cap}SubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class {cap}SubRecordUnitTest {{

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for {mod_title}")
    void testSubRecordEntityCreation() {{
        UUID parentId = UUID.randomUUID();
        {cap}SubRecordEntity sub = new {cap}SubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }}
}}
"""

    # 5. TypeScript Enterprise Service Engine File
    ts_service = f"""/**
 * FinCoreX Enterprise TypeScript Service Module: {mod_title} ({mod_name})
 */

export interface {cap}EnterpriseEvent {{
  eventId: string;
  module: '{mod_name}';
  action: string;
  payload: any;
  timestamp: string;
}}

export class {cap}EnterpriseServiceEngine {{
  private eventStore: {cap}EnterpriseEvent[] = [];

  public logEvent(action: string, payload: any): {cap}EnterpriseEvent {{
    const event: {cap}EnterpriseEvent = {{
      eventId: `evt_${{Date.now()}}_${{Math.floor(Math.random() * 89999 + 10000)}}`,
      module: '{mod_name}',
      action,
      payload,
      timestamp: new Date().toISOString()
    }};
    this.eventStore.push(event);
    return event;
  }}

  public getEventStore(): {cap}EnterpriseEvent[] {{
    return [...this.eventStore];
  }}
}}

export const {mod_name}ServiceEngineInstance = new {cap}EnterpriseServiceEngine();
"""

    return [
        (os.path.join(BASE_JAVA, mod_name, "entity", f"{cap}SubRecordEntity.java"), sec_entity),
        (os.path.join(BASE_JAVA, mod_name, "repository", f"{cap}SubRecordRepository.java"), sec_repo),
        (os.path.join(BASE_JAVA, mod_name, "mapper", f"{cap}DataMapper.java"), mapper_cls),
        (os.path.join(BASE_TEST, mod_name, f"{cap}SubRecordUnitTest.java"), sub_test),
        (os.path.join(BASE_TS, f"{mod_name}ServiceEngine.ts"), ts_service)
    ]

def main():
    total_files = 0
    for mod_name, mod_title in MODULES:
        files = generate_submodule_classes(mod_name, mod_title)
        for filepath, content in files:
            os.makedirs(os.path.dirname(filepath), exist_ok=True)
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(content)
            total_files += 1
    print(f"Generated {total_files} additional Java sub-entities, mappers, repos, tests, and TypeScript service engines.")

if __name__ == "__main__":
    main()
