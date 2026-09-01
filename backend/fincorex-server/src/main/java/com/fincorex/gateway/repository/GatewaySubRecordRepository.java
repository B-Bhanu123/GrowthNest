package com.fincorex.gateway.repository;

import com.fincorex.gateway.entity.GatewaySubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GatewaySubRecordRepository extends JpaRepository<GatewaySubRecordEntity, UUID> {
    List<GatewaySubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<GatewaySubRecordEntity> findBySubType(String subType);
}
