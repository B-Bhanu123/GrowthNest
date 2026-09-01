package com.fincorex.transaction.repository;

import com.fincorex.transaction.entity.TransactionSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionSubRecordRepository extends JpaRepository<TransactionSubRecordEntity, UUID> {
    List<TransactionSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<TransactionSubRecordEntity> findBySubType(String subType);
}
