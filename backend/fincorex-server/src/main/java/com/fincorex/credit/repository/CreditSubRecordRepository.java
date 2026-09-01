package com.fincorex.credit.repository;

import com.fincorex.credit.entity.CreditSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditSubRecordRepository extends JpaRepository<CreditSubRecordEntity, UUID> {
    List<CreditSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<CreditSubRecordEntity> findBySubType(String subType);
}
