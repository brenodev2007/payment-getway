package com.brenodev.payment_getway.DTOs;

import com.brenodev.payment_getway.Enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionWebhookDTO(
        Long transactionId,
        Long merchantId,
        BigDecimal amount,
        TransactionStatus status,
        Instant createdAt,
        Instant ocurredAt
) {
}
