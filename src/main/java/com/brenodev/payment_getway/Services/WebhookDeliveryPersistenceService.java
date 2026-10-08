
package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.WebhookMessageDTO;
import com.brenodev.payment_getway.Entity.WebhookDelivery;
import com.brenodev.payment_getway.Enums.WebhookDeliveryStatus;
import com.brenodev.payment_getway.Repositories.WebhookDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookDeliveryPersistenceService {

    private final WebhookDeliveryRepository repository;

    @Transactional(readOnly = true)
    public boolean isDelivered(UUID eventId) {
        return repository.existsByEventIdAndStatus(
                eventId,
                WebhookDeliveryStatus.DELIVERED
        );
    }

    @Transactional
    public void registerProcessing(WebhookMessageDTO message) {
        repository.findByEventId(message.eventId())
                .orElseGet(() -> repository.saveAndFlush(
                        new WebhookDelivery(message.eventId())
                ));
    }

    @Transactional
    public void markDelivered(UUID eventId) {
        WebhookDelivery delivery = repository.findByEventId(eventId)
                .orElseThrow(() -> new IllegalStateException(
                        "Registro de entrega não encontrado: " + eventId
                ));

        delivery.markDelivered();
    }
}
