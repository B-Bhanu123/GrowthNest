package com.fincorex.analytics.repository;

import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsRecordEntity, UUID> {
    Optional<AnalyticsRecordEntity> findByReferenceCode(String referenceCode);
    List<AnalyticsRecordEntity> findByOwnerId(UUID ownerId);
    List<AnalyticsRecordEntity> findByStatus(String status);
}
