package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.WebhookDelivery;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WebhookDeliveryRepository
        extends JpaRepository<WebhookDelivery, Long> {

    Optional<WebhookDelivery> findByEventId(UUID eventId);

    boolean existsByEventIdAndStatus(
            UUID eventId,
            com.brenodev.payment_getway.Enums.WebhookDeliveryStatus status
    );


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select d
        from WebhookDelivery d
        where d.eventId = :eventId
    """)
    Optional<WebhookDelivery> findByEventIdForUpdate(
            @Param("eventId") UUID eventId
    );
}