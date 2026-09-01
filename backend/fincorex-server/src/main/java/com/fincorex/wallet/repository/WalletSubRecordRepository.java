package com.fincorex.wallet.repository;

import com.fincorex.wallet.entity.WalletSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletSubRecordRepository extends JpaRepository<WalletSubRecordEntity, UUID> {
    List<WalletSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<WalletSubRecordEntity> findBySubType(String subType);
}
