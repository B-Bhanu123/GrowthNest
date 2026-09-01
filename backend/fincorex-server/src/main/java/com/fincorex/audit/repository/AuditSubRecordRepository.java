package com.fincorex.audit.repository;

import com.fincorex.audit.entity.AuditSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditSubRecordRepository extends JpaRepository<AuditSubRecordEntity, UUID> {
    List<AuditSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<AuditSubRecordEntity> findBySubType(String subType);
}
