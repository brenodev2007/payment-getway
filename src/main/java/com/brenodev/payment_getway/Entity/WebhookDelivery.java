
package com.brenodev.payment_getway.Entity;

import com.brenodev.payment_getway.Enums.WebhookDeliveryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "webhook_delivery",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_webhook_delivery_event_id",
                columnNames = "event_id"
        )
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class WebhookDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, updatable = false)
    private UUID eventId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebhookDeliveryStatus status;


    @Column(name = "lease_until")
    private Instant leaseUntil;

    @Column(nullable = false)
    private int attempts;

    @Column(name = "last_error", length = 2000)
    private String lastError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    public WebhookDelivery(UUID eventId) {
        this.eventId = eventId;
        this.status = WebhookDeliveryStatus.PROCESSING;
        this.createdAt = Instant.now();
    }

    public void markDelivered() {
        this.status = WebhookDeliveryStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

    public void reserveUntil(Instant until) {
        this.status = WebhookDeliveryStatus.PROCESSING;
        this.leaseUntil = until;
        this.attempts++;
    }

    public void markFailed(String error) {
        this.status = WebhookDeliveryStatus.FAILED;
        this.leaseUntil = null;
        this.lastError = error;
    }
}
