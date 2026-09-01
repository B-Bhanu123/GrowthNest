package com.fincorex.merchant.repository;

import com.fincorex.merchant.entity.MerchantSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantSubRecordRepository extends JpaRepository<MerchantSubRecordEntity, UUID> {
    List<MerchantSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<MerchantSubRecordEntity> findBySubType(String subType);
}
