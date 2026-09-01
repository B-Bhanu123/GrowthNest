package com.fincorex.lending.repository;

import com.fincorex.lending.entity.LendingSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LendingSubRecordRepository extends JpaRepository<LendingSubRecordEntity, UUID> {
    List<LendingSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<LendingSubRecordEntity> findBySubType(String subType);
}
