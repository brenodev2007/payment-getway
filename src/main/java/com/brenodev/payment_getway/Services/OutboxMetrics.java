package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMetrics {

    private final OutboxEventRepository repository;
    private final MeterRegistry meterRegistry;

    @Bean
    public Gauge failedOutboxEvents() {
        return Gauge.builder(
                        "payment_gateway_outbox_failed",
                        repository,
                        repo -> repo.countByStatus(
                                OutboxStatus.FAILED
                        )
                )
                .description("Quantidade de eventos Outbox em estado FAILED")
                .register(meterRegistry);
    }
}