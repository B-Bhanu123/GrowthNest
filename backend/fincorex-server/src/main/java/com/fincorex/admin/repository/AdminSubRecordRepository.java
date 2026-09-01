package com.fincorex.admin.repository;

import com.fincorex.admin.entity.AdminSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminSubRecordRepository extends JpaRepository<AdminSubRecordEntity, UUID> {
    List<AdminSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<AdminSubRecordEntity> findBySubType(String subType);
}
