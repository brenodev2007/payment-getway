package com.brenodev.payment_getway.DTOs;

import java.math.BigDecimal;

public record AccountInfoDTO(
        Long id,
        BigDecimal balance,
        BigDecimal dailyLimit,
        BigDecimal perTransactionLimit
) {}