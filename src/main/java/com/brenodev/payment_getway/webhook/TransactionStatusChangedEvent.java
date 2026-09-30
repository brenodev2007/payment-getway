package com.brenodev.payment_getway.webhook;

import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;

public record TransactionStatusChangedEvent (
        String webhookUrl, TransactionWebhookDTO payload){
}
