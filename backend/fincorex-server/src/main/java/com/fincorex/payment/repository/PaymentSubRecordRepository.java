package com.fincorex.payment.repository;

import com.fincorex.payment.entity.PaymentSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentSubRecordRepository extends JpaRepository<PaymentSubRecordEntity, UUID> {
    List<PaymentSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<PaymentSubRecordEntity> findBySubType(String subType);
}
