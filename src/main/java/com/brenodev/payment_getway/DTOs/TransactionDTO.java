package com.brenodev.payment_getway.DTOs;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDTO(

        @NotNull
        Long accountId,

        @NotNull
        Long merchantId,

        @NotNull
        @Positive
        @Digits(integer = 13, fraction = 2)
        BigDecimal amount

) {}