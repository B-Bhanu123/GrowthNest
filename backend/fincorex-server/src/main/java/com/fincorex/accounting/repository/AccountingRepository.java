package com.fincorex.accounting.repository;

import com.fincorex.accounting.entity.AccountingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountingRepository extends JpaRepository<AccountingRecordEntity, UUID> {
    Optional<AccountingRecordEntity> findByReferenceCode(String referenceCode);
    List<AccountingRecordEntity> findByOwnerId(UUID ownerId);
    List<AccountingRecordEntity> findByStatus(String status);
}
