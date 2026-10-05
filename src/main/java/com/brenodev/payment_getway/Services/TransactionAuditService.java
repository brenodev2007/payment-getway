package com.brenodev.payment_getway.Services;



import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Entity.TransactionStatusHistory;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import com.brenodev.payment_getway.Repositories.TransactionStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {
    private final TransactionStatusHistoryRepository transactionStatusHistoryRepository;


    public void record(
            Transaction transaction,
            TransactionStatus from,
            TransactionStatus to,
            String reason
    ){
        TransactionStatusHistory history = new TransactionStatusHistory(
                transaction, from, to, reason
        );

        transactionStatusHistoryRepository.save(history);
    }

}
