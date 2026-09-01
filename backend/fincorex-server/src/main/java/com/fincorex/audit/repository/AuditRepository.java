package com.fincorex.audit.repository;

import com.fincorex.audit.entity.AuditRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuditRepository extends JpaRepository<AuditRecordEntity, UUID> {
    Optional<AuditRecordEntity> findByReferenceCode(String referenceCode);
    List<AuditRecordEntity> findByOwnerId(UUID ownerId);
    List<AuditRecordEntity> findByStatus(String status);
}
