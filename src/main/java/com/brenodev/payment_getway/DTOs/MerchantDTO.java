package com.brenodev.payment_getway.DTOs;

import jakarta.validation.constraints.NotNull;

public record MerchantDTO(
        @NotNull
        Long id,
        String name
) { }
