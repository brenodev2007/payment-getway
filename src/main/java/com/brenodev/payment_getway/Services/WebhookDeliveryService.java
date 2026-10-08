package com.brenodev.payment_getway.Services;


import com.brenodev.payment_getway.DTOs.WebhookMessageDTO;
import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookDeliveryService {

    private final WebhookDeliveryReservationService reservationService;
    private final TransactionRepository transactionRepository;
    private final WebhookService webhookService;

    public void deliver(WebhookMessageDTO message) {
        var result = reservationService.reserve(message.eventId());

        switch (result) {
            case ALREADY_DELIVERED -> {
                log.info("Evento duplicado ignorado: {}", message.eventId());
                return;
            }
            case ALREADY_PROCESSING -> throw new IllegalStateException(
                    "Evento já está sendo processado: " + message.eventId()
            );
            case RESERVED -> {
                // Continua para a entrega HTTP.
            }
        }

        try {
            Transaction transaction = transactionRepository
                    .findById(message.transactionId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Transação não encontrada: "
                                    + message.transactionId()
                    ));

            var merchant = transaction.getMerchant();

            webhookService.sendWebhook(
                    merchant.getWebhookUrl(),
                    message.payload(),
                    message.eventId().toString(),
                    merchant.getWebhookSecret()
            );

            reservationService.markDelivered(message.eventId());

            log.info("Webhook entregue: {}", message.eventId());

        } catch (Exception exception) {
            log.error(
                    "Falha na entrega do webhook. eventId={}",
                    message.eventId(),
                    exception
            );
            throw exception;
        }
    }
}