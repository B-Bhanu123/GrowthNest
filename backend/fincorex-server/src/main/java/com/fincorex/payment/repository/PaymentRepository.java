package com.fincorex.payment.repository;

import com.fincorex.payment.entity.PaymentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentRecordEntity, UUID> {
    Optional<PaymentRecordEntity> findByReferenceCode(String referenceCode);
    List<PaymentRecordEntity> findByOwnerId(UUID ownerId);
    List<PaymentRecordEntity> findByStatus(String status);
}
