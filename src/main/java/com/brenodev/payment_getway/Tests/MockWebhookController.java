package com.brenodev.payment_getway.Tests;


import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@Slf4j
public class MockWebhookController {

    @PostMapping("/payments")
    public ResponseEntity<String> receiveWebhook(
            @RequestBody TransactionWebhookDTO payload
    ) {

        log.info("Webhook recebido!");
        log.info("Transação: {}", payload.transactionId());
        log.info("Status: {}", payload.status());
        log.info("Valor: {}", payload.amount());

        return ResponseEntity.ok("Webhook recebido com sucesso!");
    }
}