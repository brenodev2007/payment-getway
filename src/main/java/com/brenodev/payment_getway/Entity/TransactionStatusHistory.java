package com.brenodev.payment_getway.Entity;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transaction_status_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransactionStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private TransactionStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, updatable = false)
    private TransactionStatus toStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public TransactionStatusHistory(
            Transaction transaction,
            TransactionStatus fromStatus,
            TransactionStatus toStatus,
            String reason
    ) {
        this.transaction = transaction;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.reason = reason;
        this.createdAt = Instant.now();
    }
}