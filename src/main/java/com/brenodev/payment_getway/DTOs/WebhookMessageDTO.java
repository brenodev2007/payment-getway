
package com.brenodev.payment_getway.DTOs;

import java.util.UUID;

public record WebhookMessageDTO(
        UUID eventId,
        String eventType,
        Long transactionId,
        String payload
) {
}
