package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final WebhookSignatureService webhookSignatureService;

    @Retry(name = "webhookRetry")
    public void sendWebhook(
            String webhookUrl,
            String payload,
            String eventId,
            String webhookSecret
    ) {

        try {

            String signature =
                    webhookSignatureService.generateSignature(
                            payload,
                            webhookSecret
                    );


            log.info(
                    "Enviando webhook da transação {}",
                    eventId
            );

            restClient.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(
                            "X-Webhook-Event-Id",
                            eventId
                    )
                    .header(
                            "X-Webhook-Signature",
                            "sha256=" + signature
                    )
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();

            log.info(
                    "Webhook enviado com sucesso para transação {}",
                    eventId
            );

        } catch (Exception e) {

            log.error(
                    "Erro ao enviar webhook da transação {}",
                    eventId,
                    e
            );

            throw new RuntimeException(
                    "Falha ao enviar webhook",
                    e
            );
        }
    }
}