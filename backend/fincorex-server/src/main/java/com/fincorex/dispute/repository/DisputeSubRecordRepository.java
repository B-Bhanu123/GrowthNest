package com.fincorex.dispute.repository;

import com.fincorex.dispute.entity.DisputeSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisputeSubRecordRepository extends JpaRepository<DisputeSubRecordEntity, UUID> {
    List<DisputeSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<DisputeSubRecordEntity> findBySubType(String subType);
}
