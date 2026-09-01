package com.fincorex.admin.repository;

import com.fincorex.admin.entity.AdminRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<AdminRecordEntity, UUID> {
    Optional<AdminRecordEntity> findByReferenceCode(String referenceCode);
    List<AdminRecordEntity> findByOwnerId(UUID ownerId);
    List<AdminRecordEntity> findByStatus(String status);
}
