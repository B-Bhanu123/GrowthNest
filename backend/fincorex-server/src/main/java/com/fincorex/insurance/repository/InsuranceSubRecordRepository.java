package com.fincorex.insurance.repository;

import com.fincorex.insurance.entity.InsuranceSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceSubRecordRepository extends JpaRepository<InsuranceSubRecordEntity, UUID> {
    List<InsuranceSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<InsuranceSubRecordEntity> findBySubType(String subType);
}
