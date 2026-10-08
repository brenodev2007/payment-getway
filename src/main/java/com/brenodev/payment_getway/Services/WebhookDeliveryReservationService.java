package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.Entity.WebhookDelivery;
import com.brenodev.payment_getway.Enums.WebhookDeliveryStatus;
import com.brenodev.payment_getway.Repositories.WebhookDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WebhookDeliveryReservationService {

    private static final long LEASE_SECONDS = 120;

    private final WebhookDeliveryRepository repository;

    public enum ReservationResult {
        RESERVED,
        ALREADY_DELIVERED,
        ALREADY_PROCESSING
    }

    @Transactional
    public ReservationResult reserve(UUID eventId) {
        Instant now = Instant.now();

        var existing = repository.findByEventIdForUpdate(eventId);

        if (existing.isEmpty()) {
            WebhookDelivery delivery = new WebhookDelivery(eventId);
            delivery.reserveUntil(now.plusSeconds(LEASE_SECONDS));
            repository.saveAndFlush(delivery);
            return ReservationResult.RESERVED;
        }

        WebhookDelivery delivery = existing.get();

        if (delivery.getStatus() == WebhookDeliveryStatus.DELIVERED) {
            return ReservationResult.ALREADY_DELIVERED;
        }

        if (delivery.getStatus() == WebhookDeliveryStatus.PROCESSING
                && delivery.getLeaseUntil() != null
                && delivery.getLeaseUntil().isAfter(now)) {
            return ReservationResult.ALREADY_PROCESSING;
        }

        delivery.reserveUntil(now.plusSeconds(LEASE_SECONDS));
        return ReservationResult.RESERVED;
    }

    @Transactional
    public void markDelivered(UUID eventId) {
        WebhookDelivery delivery = repository.findByEventIdForUpdate(eventId)
                .orElseThrow(() -> new IllegalStateException(
                        "Entrega não encontrada: " + eventId
                ));

        delivery.markDelivered();
    }

    @Transactional
    public void markFailed(UUID eventId, String error) {
        WebhookDelivery delivery = repository.findByEventIdForUpdate(eventId)
                .orElseThrow(() -> new IllegalStateException(
                        "Entrega não encontrada: " + eventId
                ));

        delivery.markFailed(error);
    }
}