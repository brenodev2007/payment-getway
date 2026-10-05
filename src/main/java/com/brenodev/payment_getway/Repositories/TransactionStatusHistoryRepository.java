package com.brenodev.payment_getway.Repositories;


import com.brenodev.payment_getway.Entity.TransactionStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionStatusHistoryRepository
        extends JpaRepository<TransactionStatusHistory, Long> {

    List<TransactionStatusHistory>
    findByTransactionIdOrderByCreatedAtAsc(Long transactionId);
}