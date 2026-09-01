package com.fincorex.fraud.repository;

import com.fincorex.fraud.entity.FraudSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FraudSubRecordRepository extends JpaRepository<FraudSubRecordEntity, UUID> {
    List<FraudSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<FraudSubRecordEntity> findBySubType(String subType);
}
