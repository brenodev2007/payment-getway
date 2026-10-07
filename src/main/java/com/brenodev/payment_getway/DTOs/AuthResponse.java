package com.brenodev.payment_getway.DTOs;

public record AuthResponse(
        String token,
        String username,
        String role
) {}
