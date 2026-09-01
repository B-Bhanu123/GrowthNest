package com.fincorex.transaction.repository;

import com.fincorex.transaction.entity.TransactionRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRecordEntity, UUID> {
    Optional<TransactionRecordEntity> findByReferenceCode(String referenceCode);
    List<TransactionRecordEntity> findByOwnerId(UUID ownerId);
    List<TransactionRecordEntity> findByStatus(String status);
}
