package com.brenodev.payment_getway.DTOs;

import com.brenodev.payment_getway.Enums.TransactionStatus;

import java.time.Instant;

public record TransactionStatusHistoryDTO(
        Long id,
        TransactionStatus fromStatus,
        TransactionStatus toStatus,
        String reason,
        Instant createdAt
) {
}