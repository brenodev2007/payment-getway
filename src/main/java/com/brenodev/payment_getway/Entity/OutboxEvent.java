package com.brenodev.payment_getway.Entity;

import com.brenodev.payment_getway.Enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@Setter
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private Long aggregateId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String destinationUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OutboxStatus status;

    @Column(nullable = false)
    private Integer attempts = 0;

    @Column
    private Instant nextAttemptAt;

    @Column
    private Instant sentAt;

    @Column
    private String lastError;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();

        if (this.eventId == null) {
            this.eventId = UUID.randomUUID();
        }

        if (this.status == null) {
            this.status = OutboxStatus.PENDING;
        }

        if (this.attempts == null) {
            this.attempts = 0;
        }

        if (this.nextAttemptAt == null) {
            this.nextAttemptAt = Instant.now();
        }
    }
}
