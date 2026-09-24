package com.brenodev.payment_getway.Entity;

import com.brenodev.payment_getway.Enums.DeclineReason;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import com.brenodev.payment_getway.Exception.InvalidStateTransitionException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "Transactions")
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "decline_reason")
    private DeclineReason declineReason;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt;

    protected Transaction() {}

    public static Transaction create(Account account, Merchant merchant, BigDecimal amount) {
        Transaction t = new Transaction();
        t.account = account;
        t.merchant = merchant;
        t.amount = amount;
        t.status = TransactionStatus.PENDING;
        return t;
    }

    public void transitionTo(TransactionStatus newStatus) {

        switch (this.status) {

            case PENDING -> {
                if (newStatus == TransactionStatus.PROCESSING) {
                    this.status = newStatus;
                    return;
                }
            }

            case PROCESSING -> {
                if (newStatus == TransactionStatus.APPROVED ||
                        newStatus == TransactionStatus.DECLINED) {

                    this.status = newStatus;
                    return;
                }
            }

            case APPROVED -> {
                if (newStatus == TransactionStatus.REFUNDED) {
                    this.status = newStatus;
                    return;
                }
            }

            default -> {
            }
        }

        throw new IllegalStateException(
                "Transição inválida: " +
                        this.status + " -> " + newStatus
        );
    }

    public void decline(DeclineReason reason) {
        transitionTo(TransactionStatus.DECLINED);
        this.declineReason = reason;
    }
}