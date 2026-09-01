package com.fincorex.settlement.repository;

import com.fincorex.settlement.entity.SettlementRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SettlementRepository extends JpaRepository<SettlementRecordEntity, UUID> {
    Optional<SettlementRecordEntity> findByReferenceCode(String referenceCode);
    List<SettlementRecordEntity> findByOwnerId(UUID ownerId);
    List<SettlementRecordEntity> findByStatus(String status);
}
