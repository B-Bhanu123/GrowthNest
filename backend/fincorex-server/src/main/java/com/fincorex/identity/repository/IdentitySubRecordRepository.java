package com.fincorex.identity.repository;

import com.fincorex.identity.entity.IdentitySubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentitySubRecordRepository extends JpaRepository<IdentitySubRecordEntity, UUID> {
    List<IdentitySubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<IdentitySubRecordEntity> findBySubType(String subType);
}
