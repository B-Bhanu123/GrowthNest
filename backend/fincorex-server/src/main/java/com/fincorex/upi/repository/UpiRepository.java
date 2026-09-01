package com.fincorex.upi.repository;

import com.fincorex.upi.entity.UpiRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UpiRepository extends JpaRepository<UpiRecordEntity, UUID> {
    Optional<UpiRecordEntity> findByReferenceCode(String referenceCode);
    List<UpiRecordEntity> findByOwnerId(UUID ownerId);
    List<UpiRecordEntity> findByStatus(String status);
}
