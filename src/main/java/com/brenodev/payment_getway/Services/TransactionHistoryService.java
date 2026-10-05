package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionStatusHistoryDTO;
import com.brenodev.payment_getway.Entity.TransactionStatusHistory;
import com.brenodev.payment_getway.Exception.ResourceNotFoundException;
import com.brenodev.payment_getway.Repositories.TransactionRepository;
import com.brenodev.payment_getway.Repositories.TransactionStatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionHistoryService {

    private final TransactionRepository transactionRepository;
    private final TransactionStatusHistoryRepository historyRepository;

    @Transactional(readOnly = true)
    public List<TransactionStatusHistoryDTO> findByTransactionId(
            Long transactionId
    ) {

        transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transação não encontrada"
                        )
                );

        return historyRepository
                .findByTransactionIdOrderByCreatedAtAsc(transactionId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TransactionStatusHistoryDTO toDTO(
            TransactionStatusHistory history
    ) {
        return new TransactionStatusHistoryDTO(
                history.getId(),
                history.getFromStatus(),
                history.getToStatus(),
                history.getReason(),
                history.getCreatedAt()
        );
    }
}