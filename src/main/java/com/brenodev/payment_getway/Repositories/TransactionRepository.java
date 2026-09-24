package com.brenodev.payment_getway.Repositories;

import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    //Limite diário
    @Query("""
        select coalesce(sum(t.amount), 0) from PaymentTransaction t
        where t.account.id = :accountId and t.status = :status and t.createdAt >= :since
        """)
    BigDecimal sumAmountSince(@Param("accountId") Long accountId,
                              @Param("status") TransactionStatus status,
                              @Param("since") Instant since);
}


