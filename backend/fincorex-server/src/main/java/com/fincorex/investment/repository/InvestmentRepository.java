package com.fincorex.investment.repository;

import com.fincorex.investment.entity.InvestmentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentRecordEntity, UUID> {
    Optional<InvestmentRecordEntity> findByReferenceCode(String referenceCode);
    List<InvestmentRecordEntity> findByOwnerId(UUID ownerId);
    List<InvestmentRecordEntity> findByStatus(String status);
}
