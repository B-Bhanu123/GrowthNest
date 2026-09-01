package com.fincorex.refund.repository;

import com.fincorex.refund.entity.RefundSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefundSubRecordRepository extends JpaRepository<RefundSubRecordEntity, UUID> {
    List<RefundSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<RefundSubRecordEntity> findBySubType(String subType);
}
