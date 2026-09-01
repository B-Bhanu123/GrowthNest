package com.fincorex.upi.repository;

import com.fincorex.upi.entity.UpiSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UpiSubRecordRepository extends JpaRepository<UpiSubRecordEntity, UUID> {
    List<UpiSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<UpiSubRecordEntity> findBySubType(String subType);
}
