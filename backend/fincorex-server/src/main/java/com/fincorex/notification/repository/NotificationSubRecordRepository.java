package com.fincorex.notification.repository;

import com.fincorex.notification.entity.NotificationSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationSubRecordRepository extends JpaRepository<NotificationSubRecordEntity, UUID> {
    List<NotificationSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<NotificationSubRecordEntity> findBySubType(String subType);
}
