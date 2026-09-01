package com.fincorex.merchant.repository;

import com.fincorex.merchant.entity.MerchantRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantRecordEntity, UUID> {
    Optional<MerchantRecordEntity> findByReferenceCode(String referenceCode);
    List<MerchantRecordEntity> findByOwnerId(UUID ownerId);
    List<MerchantRecordEntity> findByStatus(String status);
}
