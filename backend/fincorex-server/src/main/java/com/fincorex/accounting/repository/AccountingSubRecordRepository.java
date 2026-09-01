package com.fincorex.accounting.repository;

import com.fincorex.accounting.entity.AccountingSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountingSubRecordRepository extends JpaRepository<AccountingSubRecordEntity, UUID> {
    List<AccountingSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<AccountingSubRecordEntity> findBySubType(String subType);
}
