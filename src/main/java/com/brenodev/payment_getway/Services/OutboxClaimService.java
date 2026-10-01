
package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxClaimService {

    private final OutboxEventRepository repository;

    @Transactional
    public List<OutboxEvent> claimPendingEvents(int batchSize) {

        List<OutboxEvent> events =
                repository
                        .findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
                                OutboxStatus.PENDING,
                                Instant.now(),
                                (Pageable) PageRequest.of(0, batchSize)
                        );

        Instant now = Instant.now();

        for (OutboxEvent event : events) {
            event.setStatus(OutboxStatus.PROCESSING);
            event.setProcessingStartedAt(now);
        }

        repository.saveAll(events);

        return events;
    }

    @Transactional
    public int recoverStuckEvents(Instant cutoff) {

        List<OutboxEvent> events =
                repository.findByStatusAndProcessingStartedAtLessThanEqual(
                        OutboxStatus.PROCESSING,
                        cutoff
                );

        Instant now = Instant.now();

        for (OutboxEvent event : events) {
            event.setStatus(OutboxStatus.PENDING);
            event.setProcessingStartedAt(null);
            event.setNextAttemptAt(now);
        }

        repository.saveAll(events);

        return events.size();
    }
}