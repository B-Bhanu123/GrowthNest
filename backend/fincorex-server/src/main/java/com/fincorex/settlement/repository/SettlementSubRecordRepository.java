package com.fincorex.settlement.repository;

import com.fincorex.settlement.entity.SettlementSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SettlementSubRecordRepository extends JpaRepository<SettlementSubRecordEntity, UUID> {
    List<SettlementSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<SettlementSubRecordEntity> findBySubType(String subType);
}
