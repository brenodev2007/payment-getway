
package com.brenodev.payment_getway.Config;

import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import com.brenodev.payment_getway.Services.OutboxClaimService;
import com.brenodev.payment_getway.Services.RabbitMQPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxWorker {

    private static final long PROCESSING_TIMEOUT_MINUTES = 5;

    private final OutboxEventRepository outboxRepository;
    private final OutboxClaimService claimService;
    private final RabbitMQPublisherService publisher;
    private final OutboxProperties properties;

    @Scheduled(fixedDelayString = "${webhook.outbox.polling-interval:5000}")
    public void processPendingEvents() {

        List<OutboxEvent> events =
                claimService.claimPendingEvents(
                        properties.getBatchSize()
                );

        for (OutboxEvent event : events) {
            processEvent(event);
        }
    }

    @Scheduled(fixedDelayString = "${webhook.outbox.recovery-interval:60000}")
    public void recoverStuckEvents() {

        Instant cutoff = Instant.now()
                .minusSeconds(PROCESSING_TIMEOUT_MINUTES * 60);

        int recovered = claimService.recoverStuckEvents(cutoff);

        if (recovered > 0) {
            log.warn(
                    "{} eventos presos em PROCESSING foram recuperados.",
                    recovered
            );
        }
    }

    private void processEvent(OutboxEvent event) {

        try {
            // Publica e aguarda a confirmação do RabbitMQ.
            publisher.publish(event);

            // Só marca como SENT após confirmação positiva.
            markAsSent(event);

        } catch (Exception e) {
            scheduleRetry(event, e);
        }
    }

    private void markAsSent(OutboxEvent event) {

        event.setStatus(OutboxStatus.SENT);
        event.setSentAt(Instant.now());
        event.setLastError(null);
        event.setProcessingStartedAt(null);
        event.setNextAttemptAt(null);

        outboxRepository.save(event);

        log.info(
                "Evento publicado no RabbitMQ. eventId={}, type={}",
                event.getEventId(),
                event.getEventType()
        );
    }

    private void scheduleRetry(
            OutboxEvent event,
            Exception exception
    ) {

        int attempts = event.getAttempts() + 1;

        event.setAttempts(attempts);
        event.setLastError(
                truncate(exception.getMessage(), 2000)
        );
        event.setProcessingStartedAt(null);

        if (attempts >= properties.getMaxAttempts()) {

            event.setStatus(OutboxStatus.FAILED);
            event.setNextAttemptAt(null);

            log.error(
                    "Evento {} atingiu o limite de tentativas. Erro: {}",
                    event.getEventId(),
                    event.getLastError()
            );

        } else {

            long delay = calculateBackoff(attempts);

            event.setStatus(OutboxStatus.PENDING);
            event.setNextAttemptAt(
                    Instant.now().plusSeconds(delay)
            );

            log.warn(
                    "Falha ao publicar evento {}. Tentativa {} de {}. "
                            + "Nova tentativa em {} segundos. Erro: {}",
                    event.getEventId(),
                    attempts,
                    properties.getMaxAttempts(),
                    delay,
                    event.getLastError()
            );
        }

        outboxRepository.save(event);
    }

    private long calculateBackoff(int attempts) {

        long initialDelay = properties.getInitialDelay();
        long maxDelay = properties.getMaxDelay();

        int exponent = Math.min(attempts - 1, 30);

        long delay;

        try {
            delay = Math.multiplyExact(
                    initialDelay,
                    1L << exponent
            );
        } catch (ArithmeticException e) {
            delay = maxDelay;
        }

        return Math.min(delay, maxDelay);
    }

    private String truncate(String message, int maxLength) {

        if (message == null) {
            return "Erro desconhecido";
        }

        return message.length() <= maxLength
                ? message
                : message.substring(0, maxLength);
    }
}
