package com.brenodev.payment_getway.DTOs;

import com.brenodev.payment_getway.Enums.OutboxStatus;

import java.time.Instant;
import java.util.UUID;

public record FailedOutboxEventDTO(
        Long id,
        UUID eventId,
        String eventType,
        Long aggregateId,
        OutboxStatus status,
        Integer attempts,
        Instant nextAttemptAt,
        String lastError,
        Instant createdAt
) {
}
