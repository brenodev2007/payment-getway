package com.brenodev.payment_getway.webhook;

import com.brenodev.payment_getway.Services.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransactionWebhookListener {

    private final WebhookService webhookService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(TransactionStatusChangedEvent event) {

        if (event.webhookUrl() == null ||
                event.webhookUrl().isBlank()) {
            return;
        }

        webhookService.sendWebhook(
                event.webhookUrl(),
                event.payload()
        );
    }
}