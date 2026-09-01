package com.fincorex.identity.repository;

import com.fincorex.identity.entity.IdentityRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityRecordEntity, UUID> {
    Optional<IdentityRecordEntity> findByReferenceCode(String referenceCode);
    List<IdentityRecordEntity> findByOwnerId(UUID ownerId);
    List<IdentityRecordEntity> findByStatus(String status);
}
