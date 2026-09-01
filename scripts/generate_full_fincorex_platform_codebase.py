import os

BASE_JAVA = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "main", "java", "com", "fincorex")
BASE_TEST = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "test", "java", "com", "fincorex")
BASE_TS_MODELS = os.path.join(os.getcwd(), "backend", "enterprise-models")

MODULES = [
    "identity",
    "customer",
    "merchant",
    "payment",
    "wallet",
    "upi",
    "transaction",
    "ledger",
    "settlement",
    "reconciliation",
    "refund",
    "dispute",
    "lending",
    "credit",
    "investment",
    "insurance",
    "fraud",
    "accounting",
    "expense",
    "analytics",
    "notification",
    "audit",
    "admin",
    "gateway"
]

def generate_expanded_java_module(mod):
    cap = mod.capitalize()
    
    # 1. CreateRequest DTO
    create_req = f"""package com.fincorex.{mod}.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Enterprise Create Request DTO for {cap} Domain
 */
public class Create{cap}Request {{

    @NotBlank(message = "Reference code cannot be blank")
    @Size(min = 4, max = 100, message = "Reference code must be between 4 and 100 characters")
    private String referenceCode;

    @NotNull(message = "Owner UUID is required")
    private UUID ownerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{{3}}$", message = "Currency must be a 3-letter ISO code")
    private String currency;

    private String category;
    private String notes;

    public Create{cap}Request() {{}}

    public Create{cap}Request(String referenceCode, UUID ownerId, BigDecimal amount, String currency) {{
        this.referenceCode = referenceCode;
        this.ownerId = ownerId;
        this.amount = amount;
        this.currency = currency;
    }}

    public String getReferenceCode() {{ return referenceCode; }}
    public void setReferenceCode(String referenceCode) {{ this.referenceCode = referenceCode; }}

    public UUID getOwnerId() {{ return ownerId; }}
    public void setOwnerId(UUID ownerId) {{ this.ownerId = ownerId; }}

    public BigDecimal getAmount() {{ return amount; }}
    public void setAmount(BigDecimal amount) {{ this.amount = amount; }}

    public String getCurrency() {{ return currency; }}
    public void setCurrency(String currency) {{ this.currency = currency; }}

    public String getCategory() {{ return category; }}
    public void setCategory(String category) {{ this.category = category; }}

    public String getNotes() {{ return notes; }}
    public void setNotes(String notes) {{ this.notes = notes; }}
}}
"""

    # 2. JPA Specification
    spec = f"""package com.fincorex.{mod}.specification;

import com.fincorex.{mod}.entity.{cap}RecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for {cap} Queries
 */
public class {cap}Specification {{

    public static Specification<{cap}RecordEntity> hasOwnerId(UUID ownerId) {{
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }}

    public static Specification<{cap}RecordEntity> hasStatus(String status) {{
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }}

    public static Specification<{cap}RecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {{
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }}

    public static Specification<{cap}RecordEntity> createdAfter(LocalDateTime fromDate) {{
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }}
}}
"""

    # 3. Kafka Domain Event Handler
    event_handler = f"""package com.fincorex.{mod}.event;

import com.fincorex.{mod}.dto.{cap}DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for {cap} Domain Events
 */
@Component
public class {cap}EventHandler {{

    private static final Logger log = LoggerFactory.getLogger({cap}EventHandler.class);

    public void publish{cap}CreatedEvent({cap}DTO dto) {{
        log.info("[KAFKA-EVENT] Publishing {cap}CreatedEvent - ID: {{}}, Ref: {{}}, Amount: {{}} at {{}}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }}

    public void handleIncoming{cap}Message(String payload) {{
        log.info("[KAFKA-CONSUMER] Consumed message in {mod} topic: {{}}", payload);
    }}
}}
"""

    # 4. Domain Exception Class
    exception_cls = f"""package com.fincorex.{mod}.exception;

/**
 * Specific Business Exception for {cap} Operations
 */
public class {cap}DomainException extends RuntimeException {{
    private final String errorCode;

    public {cap}DomainException(String message) {{
        super(message);
        this.errorCode = "{mod.upper()}_ERR_GENERAL";
    }}

    public {cap}DomainException(String errorCode, String message) {{
        super(message);
        this.errorCode = errorCode;
    }}

    public String getErrorCode() {{ return errorCode; }}
}}
"""

    # 5. Full JUnit 5 Test Class
    test_cls = f"""package com.fincorex.{mod};

import com.fincorex.{mod}.dto.{cap}DTO;
import com.fincorex.{mod}.dto.Create{cap}Request;
import com.fincorex.{mod}.entity.{cap}RecordEntity;
import com.fincorex.{mod}.repository.{cap}Repository;
import com.fincorex.{mod}.service.{cap}Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class {cap}ServiceUnitTest {{

    @Mock
    private {cap}Repository repository;

    @InjectMocks
    private {cap}Service service;

    private {cap}RecordEntity entity;
    private UUID ownerId;

    @BeforeEach
    void setUp() {{
        ownerId = UUID.randomUUID();
        entity = new {cap}RecordEntity("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());
    }}

    @Test
    void testCreateRecordSuccess() {{
        when(repository.save(any({cap}RecordEntity.class))).thenReturn(entity);

        {cap}DTO result = service.createRecord("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");

        assertNotNull(result);
        assertEquals("REF-10029", result.getReferenceCode());
        assertEquals(new BigDecimal("450.00"), result.getAmount());
        verify(repository, times(1)).save(any());
    }}

    @Test
    void testGetByReferenceCodeSuccess() {{
        when(repository.findByReferenceCode("REF-10029")).thenReturn(Optional.of(entity));

        {cap}DTO result = service.getByReferenceCode("REF-10029");

        assertNotNull(result);
        assertEquals(ownerId, result.getOwnerId());
    }}

    @Test
    void testGetByReferenceCodeNotFoundThrows() {{
        when(repository.findByReferenceCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getByReferenceCode("INVALID"));
    }}
}}
"""

    return [
        (os.path.join(BASE_JAVA, mod, "dto", f"Create{cap}Request.java"), create_req),
        (os.path.join(BASE_JAVA, mod, "specification", f"{cap}Specification.java"), spec),
        (os.path.join(BASE_JAVA, mod, "event", f"{cap}EventHandler.java"), event_handler),
        (os.path.join(BASE_JAVA, mod, "exception", f"{cap}DomainException.java"), exception_cls),
        (os.path.join(BASE_TEST, mod, f"{cap}ServiceUnitTest.java"), test_cls)
    ]

def generate_ts_enterprise_models(mod):
    cap = mod.capitalize()
    ts_code = f"""/**
 * FinCoreX Enterprise TypeScript Schema & State Manager
 * Module: {mod}
 */

export interface Enterprise{cap}Schema {{
  id: string;
  referenceCode: string;
  ownerId: string;
  amount: number;
  currency: string;
  status: 'PENDING' | 'ACTIVE' | 'COMPLETED' | 'FAILED' | 'REJECTED';
  metadata: {{
    ipAddress?: string;
    userAgent?: string;
    channel?: string;
    riskScore?: number;
    auditTags?: string[];
  }};
  createdAt: string;
  updatedAt: string;
}}

export class Enterprise{cap}ModelManager {{
  private records: Map<string, Enterprise{cap}Schema> = new Map();

  public registerRecord(data: Omit<Enterprise{cap}Schema, 'id' | 'createdAt' | 'updatedAt'>): Enterprise{cap}Schema {{
    const id = `{mod}_rec_${{Date.now()}}_${{Math.floor(Math.random() * 89999 + 10000)}}`;
    const record: Enterprise{cap}Schema = {{
      ...data,
      id,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    }};
    this.records.set(id, record);
    return record;
  }}

  public findById(id: string): Enterprise{cap}Schema | undefined {{
    return this.records.get(id);
  }}

  public filterByStatus(status: Enterprise{cap}Schema['status']): Enterprise{cap}Schema[] {{
    return Array.from(this.records.values()).filter(r => r.status === status);
  }}

  public computeAggregateTotal(): number {{
    let total = 0;
    for (const record of this.records.values()) {{
      total += record.amount;
    }}
    return Number(total.toFixed(2));
  }}
}}

export const {mod}EnterpriseManagerInstance = new Enterprise{cap}ModelManager();
"""
    return os.path.join(BASE_TS_MODELS, f"{mod}EnterpriseModel.ts"), ts_code

def main():
    java_count = 0
    ts_count = 0
    
    for mod in MODULES:
        java_files = generate_expanded_java_module(mod)
        for filepath, content in java_files:
            os.makedirs(os.path.dirname(filepath), exist_ok=True)
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(content)
            java_count += 1
            
        ts_filepath, ts_content = generate_ts_enterprise_models(mod)
        os.makedirs(os.path.dirname(ts_filepath), exist_ok=True)
        with open(ts_filepath, "w", encoding="utf-8") as f:
            f.write(ts_content)
        ts_count += 1

    print(f"Successfully generated {java_count} expanded Java enterprise classes & {ts_count} TypeScript model managers.")

if __name__ == "__main__":
    main()
