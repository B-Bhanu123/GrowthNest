package com.fincorex.wallet.mapper;

import com.fincorex.wallet.dto.WalletDTO;
import com.fincorex.wallet.entity.WalletRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Stored-Value Digital Wallet
 */
@Component
public class WalletDataMapper {

    public WalletDTO toDTO(WalletRecordEntity entity) {
        if (entity == null) return null;
        WalletDTO dto = new WalletDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public WalletRecordEntity toEntity(WalletDTO dto) {
        if (dto == null) return null;
        WalletRecordEntity entity = new WalletRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
