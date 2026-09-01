package com.fincorex.credit.repository;

import com.fincorex.credit.entity.CreditRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditRepository extends JpaRepository<CreditRecordEntity, UUID> {
    Optional<CreditRecordEntity> findByReferenceCode(String referenceCode);
    List<CreditRecordEntity> findByOwnerId(UUID ownerId);
    List<CreditRecordEntity> findByStatus(String status);
}
