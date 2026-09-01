package com.fincorex.ledger.repository;

import com.fincorex.ledger.entity.LedgerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LedgerRepository extends JpaRepository<LedgerRecordEntity, UUID> {
    Optional<LedgerRecordEntity> findByReferenceCode(String referenceCode);
    List<LedgerRecordEntity> findByOwnerId(UUID ownerId);
    List<LedgerRecordEntity> findByStatus(String status);
}
