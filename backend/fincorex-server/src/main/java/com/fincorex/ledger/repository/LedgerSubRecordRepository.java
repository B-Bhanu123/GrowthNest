package com.fincorex.ledger.repository;

import com.fincorex.ledger.entity.LedgerSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LedgerSubRecordRepository extends JpaRepository<LedgerSubRecordEntity, UUID> {
    List<LedgerSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<LedgerSubRecordEntity> findBySubType(String subType);
}
