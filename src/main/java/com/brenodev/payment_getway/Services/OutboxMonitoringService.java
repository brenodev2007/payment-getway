package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.FailedOutboxEventDTO;
import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxMonitoringService {

    private final OutboxEventRepository repository;

    @Transactional
    public List<FailedOutboxEventDTO> findFailedEvents() {

        return repository.findByStatus(OutboxStatus.FAILED)
                .stream()
                .map(event -> new FailedOutboxEventDTO(
                        event.getId(),
                        event.getEventId(),
                        event.getEventType(),
                        event.getAggregateId(),
                        event.getStatus(),
                        event.getAttempts(),
                        event.getNextAttemptAt(),
                        event.getLastError(),
                        event.getCreatedAt()
                ))
                .toList();
    }

    @Transactional
    public long countFailedEvents() {
        return repository.countByStatus(OutboxStatus.FAILED);
    }
}