package com.brenodev.payment_getway.Config;


import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import com.brenodev.payment_getway.Services.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxWorker {

    private final OutboxEventRepository outboxRepository;
    private final WebhookService webhookService;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    public void processPendingEvents() {

        List<OutboxEvent> events =
                outboxRepository
                        .findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
                                OutboxStatus.PENDING,
                                Instant.now()
                        );

        for (OutboxEvent event : events) {
            processEvent(event);
        }
    }

    private void processEvent(OutboxEvent event) {

        try {
            event.setStatus(OutboxStatus.PROCESSING);
            event.setAttempts(event.getAttempts() + 1);

            outboxRepository.save(event);

            TransactionWebhookDTO payload =
                    objectMapper.readValue(
                            event.getPayload(),
                            TransactionWebhookDTO.class
                    );

            webhookService.sendWebhook(
                    event.getDestinationUrl(),
                    payload
            );

            event.setStatus(OutboxStatus.SENT);
            event.setSentAt(Instant.now());
            event.setLastError(null);

            outboxRepository.save(event);

            log.info(
                    "Evento {} enviado com sucesso",
                    event.getEventId()
            );

        } catch (Exception e) {

            event.setStatus(OutboxStatus.FAILED);
            event.setLastError(e.getMessage());

            outboxRepository.save(event);

            log.error(
                    "Falha ao processar evento {}: {}",
                    event.getEventId(),
                    e.getMessage()
            );
        }
    }
}
