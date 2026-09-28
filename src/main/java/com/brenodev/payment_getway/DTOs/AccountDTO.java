package com.brenodev.payment_getway.DTOs;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AccountDTO (
        @NotNull
        Long id,
        BigDecimal balance,
        BigDecimal perTransactionLimit,
        BigDecimal dailyLimit
){
}
