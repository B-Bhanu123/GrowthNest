package com.fincorex.analytics.repository;

import com.fincorex.analytics.entity.AnalyticsSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsSubRecordRepository extends JpaRepository<AnalyticsSubRecordEntity, UUID> {
    List<AnalyticsSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<AnalyticsSubRecordEntity> findBySubType(String subType);
}
