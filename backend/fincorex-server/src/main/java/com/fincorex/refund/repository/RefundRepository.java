package com.fincorex.refund.repository;

import com.fincorex.refund.entity.RefundRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefundRepository extends JpaRepository<RefundRecordEntity, UUID> {
    Optional<RefundRecordEntity> findByReferenceCode(String referenceCode);
    List<RefundRecordEntity> findByOwnerId(UUID ownerId);
    List<RefundRecordEntity> findByStatus(String status);
}
