package com.fincorex.wallet.repository;

import com.fincorex.wallet.entity.WalletRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<WalletRecordEntity, UUID> {
    Optional<WalletRecordEntity> findByReferenceCode(String referenceCode);
    List<WalletRecordEntity> findByOwnerId(UUID ownerId);
    List<WalletRecordEntity> findByStatus(String status);
}
