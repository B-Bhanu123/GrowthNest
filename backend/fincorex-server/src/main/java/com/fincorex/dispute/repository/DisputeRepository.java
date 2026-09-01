package com.fincorex.dispute.repository;

import com.fincorex.dispute.entity.DisputeRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DisputeRepository extends JpaRepository<DisputeRecordEntity, UUID> {
    Optional<DisputeRecordEntity> findByReferenceCode(String referenceCode);
    List<DisputeRecordEntity> findByOwnerId(UUID ownerId);
    List<DisputeRecordEntity> findByStatus(String status);
}
