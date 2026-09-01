import os

BASE_DIR = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "main", "java", "com", "fincorex")

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

def generate_module_code(mod_name):
    mod_cap = mod_name.capitalize()
    
    # Entity Class
    entity_code = f"""package com.fincorex.{mod_name}.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * FinCoreX Enterprise {mod_cap} Domain Entity
 * Managed domain table within PostgreSQL schema: {mod_name}
 */
@Entity
@Table(name = "{mod_name}_records", schema = "{mod_name}")
public class {mod_cap}RecordEntity {{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "reference_code", nullable = false, unique = true, length = 100)
    private String referenceCode;

    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(name = "amount", precision = 18, scale = 4)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public {mod_cap}RecordEntity() {{
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }}

    public {mod_cap}RecordEntity(String referenceCode, UUID ownerId, BigDecimal amount, String status) {{
        this.referenceCode = referenceCode;
        this.ownerId = ownerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }}

    public UUID getId() {{ return id; }}
    public void setId(UUID id) {{ this.id = id; }}

    public String getReferenceCode() {{ return referenceCode; }}
    public void setReferenceCode(String referenceCode) {{ this.referenceCode = referenceCode; }}

    public UUID getOwnerId() {{ return ownerId; }}
    public void setOwnerId(UUID ownerId) {{ this.ownerId = ownerId; }}

    public BigDecimal getAmount() {{ return amount; }}
    public void setAmount(BigDecimal amount) {{ this.amount = amount; }}

    public String getStatus() {{ return status; }}
    public void setStatus(String status) {{ this.status = status; }}

    public String getMetadataJson() {{ return metadataJson; }}
    public void setMetadataJson(String metadataJson) {{ this.metadataJson = metadataJson; }}

    public LocalDateTime getCreatedAt() {{ return createdAt; }}
    public void setCreatedAt(LocalDateTime createdAt) {{ this.createdAt = createdAt; }}

    public LocalDateTime getUpdatedAt() {{ return updatedAt; }}
    public void setUpdatedAt(LocalDateTime updatedAt) {{ this.updatedAt = updatedAt; }}
}}
"""

    # DTO Class
    dto_code = f"""package com.fincorex.{mod_name}.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * FinCoreX Data Transfer Object for {mod_cap} Module
 */
public class {mod_cap}DTO {{
    private UUID id;
    private String referenceCode;
    private UUID ownerId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    public {mod_cap}DTO() {{}}

    public {mod_cap}DTO(UUID id, String referenceCode, UUID ownerId, BigDecimal amount, String status, LocalDateTime createdAt) {{
        this.id = id;
        this.referenceCode = referenceCode;
        this.ownerId = ownerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }}

    public UUID getId() {{ return id; }}
    public void setId(UUID id) {{ this.id = id; }}

    public String getReferenceCode() {{ return referenceCode; }}
    public void setReferenceCode(String referenceCode) {{ this.referenceCode = referenceCode; }}

    public UUID getOwnerId() {{ return ownerId; }}
    public void setOwnerId(UUID ownerId) {{ this.ownerId = ownerId; }}

    public BigDecimal getAmount() {{ return amount; }}
    public void setAmount(BigDecimal amount) {{ this.amount = amount; }}

    public String getStatus() {{ return status; }}
    public void setStatus(String status) {{ this.status = status; }}

    public LocalDateTime getCreatedAt() {{ return createdAt; }}
    public void setCreatedAt(LocalDateTime createdAt) {{ this.createdAt = createdAt; }}
}}
"""

    # Repository Interface
    repo_code = f"""package com.fincorex.{mod_name}.repository;

import com.fincorex.{mod_name}.entity.{mod_cap}RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface {mod_cap}Repository extends JpaRepository<{mod_cap}RecordEntity, UUID> {{
    Optional<{mod_cap}RecordEntity> findByReferenceCode(String referenceCode);
    List<{mod_cap}RecordEntity> findByOwnerId(UUID ownerId);
    List<{mod_cap}RecordEntity> findByStatus(String status);
}}
"""

    # Service Class
    service_code = f"""package com.fincorex.{mod_name}.service;

import com.fincorex.{mod_name}.dto.{mod_cap}DTO;
import com.fincorex.{mod_name}.entity.{mod_cap}RecordEntity;
import com.fincorex.{mod_name}.repository.{mod_cap}Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class {mod_cap}Service {{

    private final {mod_cap}Repository repository;

    @Autowired
    public {mod_cap}Service({mod_cap}Repository repository) {{
        this.repository = repository;
    }}

    public {mod_cap}DTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {{
        {mod_cap}RecordEntity entity = new {mod_cap}RecordEntity(referenceCode, ownerId, amount, status);
        {mod_cap}RecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }}

    @Transactional(readOnly = true)
    public {mod_cap}DTO getByReferenceCode(String referenceCode) {{
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("{mod_cap} record not found for ref: " + referenceCode));
    }}

    @Transactional(readOnly = true)
    public List<{mod_cap}DTO> getByOwnerId(UUID ownerId) {{
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }}

    public {mod_cap}DTO updateStatus(UUID id, String newStatus) {{
        {mod_cap}RecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        {mod_cap}RecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }}

    private {mod_cap}DTO mapToDTO({mod_cap}RecordEntity entity) {{
        return new {mod_cap}DTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }}
}}
"""

    # Controller Class
    controller_code = f"""package com.fincorex.{mod_name}.controller;

import com.fincorex.{mod_name}.dto.{mod_cap}DTO;
import com.fincorex.{mod_name}.service.{mod_cap}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/{mod_name}")
public class {mod_cap}Controller {{

    private final {mod_cap}Service service;

    @Autowired
    public {mod_cap}Controller({mod_cap}Service service) {{
        this.service = service;
    }}

    @PostMapping
    public ResponseEntity<{mod_cap}DTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {{
        {mod_cap}DTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }}

    @GetMapping("/ref/{{referenceCode}}")
    public ResponseEntity<{mod_cap}DTO> getByReference(@PathVariable String referenceCode) {{
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }}

    @GetMapping("/owner/{{ownerId}}")
    public ResponseEntity<List<{mod_cap}DTO>> getByOwner(@PathVariable UUID ownerId) {{
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }}

    @PutMapping("/{{id}}/status")
    public ResponseEntity<{mod_cap}DTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {{
        return ResponseEntity.ok(service.updateStatus(id, status));
    }}
}}
"""

    return [
        (os.path.join(BASE_DIR, mod_name, "entity", f"{mod_cap}RecordEntity.java"), entity_code),
        (os.path.join(BASE_DIR, mod_name, "dto", f"{mod_cap}DTO.java"), dto_code),
        (os.path.join(BASE_DIR, mod_name, "repository", f"{mod_cap}Repository.java"), repo_code),
        (os.path.join(BASE_DIR, mod_name, "service", f"{mod_cap}Service.java"), service_code),
        (os.path.join(BASE_DIR, mod_name, "controller", f"{mod_cap}Controller.java"), controller_code)
    ]

def main():
    total_files = 0
    for mod in MODULES:
        files = generate_module_code(mod)
        for filepath, content in files:
            os.makedirs(os.path.dirname(filepath), exist_ok=True)
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(content)
            total_files += 1
    print(f"Successfully generated {total_files} Java microservice files across {len(MODULES)} domain modules.")

if __name__ == "__main__":
    main()
