package com.fincorex.wallet.event;

import com.fincorex.wallet.dto.WalletDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Event Subscriber & Publisher for Wallet Domain Events
 */
@Component
public class WalletEventHandler {

    private static final Logger log = LoggerFactory.getLogger(WalletEventHandler.class);

    public void publishWalletCreatedEvent(WalletDTO dto) {
        log.info("[KAFKA-EVENT] Publishing WalletCreatedEvent - ID: {}, Ref: {}, Amount: {} at {}",
                dto.getId(), dto.getReferenceCode(), dto.getAmount(), LocalDateTime.now());
        // Simulating Event Streaming over Kafka broker
    }

    public void handleIncomingWalletMessage(String payload) {
        log.info("[KAFKA-CONSUMER] Consumed message in wallet topic: {}", payload);
    }
}
