package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final RestClient restClient;


    @Async
    public void sendWebhook(String webhookUrl, TransactionWebhookDTO payload) {

            restClient.post()
                    .uri(webhookUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(payload)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Webhook posted successfully", payload.transactionId());

    }
}
