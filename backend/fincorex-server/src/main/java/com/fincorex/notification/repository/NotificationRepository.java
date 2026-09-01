package com.fincorex.notification.repository;

import com.fincorex.notification.entity.NotificationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationRecordEntity, UUID> {
    Optional<NotificationRecordEntity> findByReferenceCode(String referenceCode);
    List<NotificationRecordEntity> findByOwnerId(UUID ownerId);
    List<NotificationRecordEntity> findByStatus(String status);
}
