package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.awt.print.Pageable;
import java.time.Instant;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
            OutboxStatus status,
            Instant now
    );
    
    
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutboxEvent>
    findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
            OutboxStatus status,
            Instant now,
            Pageable pageable
    );

    List<OutboxEvent> findByStatusAndProcessingStartedAtLessThanEqual(OutboxStatus outboxStatus, Instant cutoff);
}
