package com.fincorex.lending.repository;

import com.fincorex.lending.entity.LendingRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LendingRepository extends JpaRepository<LendingRecordEntity, UUID> {
    Optional<LendingRecordEntity> findByReferenceCode(String referenceCode);
    List<LendingRecordEntity> findByOwnerId(UUID ownerId);
    List<LendingRecordEntity> findByStatus(String status);
}
