package com.fincorex.investment.repository;

import com.fincorex.investment.entity.InvestmentSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvestmentSubRecordRepository extends JpaRepository<InvestmentSubRecordEntity, UUID> {
    List<InvestmentSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<InvestmentSubRecordEntity> findBySubType(String subType);
}
