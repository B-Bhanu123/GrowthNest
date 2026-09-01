package com.fincorex.gateway.repository;

import com.fincorex.gateway.entity.GatewayRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GatewayRepository extends JpaRepository<GatewayRecordEntity, UUID> {
    Optional<GatewayRecordEntity> findByReferenceCode(String referenceCode);
    List<GatewayRecordEntity> findByOwnerId(UUID ownerId);
    List<GatewayRecordEntity> findByStatus(String status);
}
