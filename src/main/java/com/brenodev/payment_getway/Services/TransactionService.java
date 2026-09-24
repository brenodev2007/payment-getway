package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionDTO;
import com.brenodev.payment_getway.Entity.Account;
import com.brenodev.payment_getway.Entity.Merchant;
import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import com.brenodev.payment_getway.Repositories.AccountRepository;
import com.brenodev.payment_getway.Repositories.MerchantRepository;
import com.brenodev.payment_getway.Repositories.TransactionRepository;
import com.brenodev.payment_getway.Exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MerchantRepository merchantRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    @Transactional
    public Transaction create(TransactionDTO dto) {

        Account account = accountRepository
                .findById(dto.accountId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conta não encontrada"));

        Merchant merchant = merchantRepository
                .findById(dto.merchantId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Merchant não encontrado"));

        Transaction transaction = Transaction.create(
                account,
                merchant,
                dto.amount()
        );

        transaction.transitionTo(TransactionStatus.PROCESSING);

        // processamento...

        transaction.transitionTo(TransactionStatus.APPROVED);

        return transactionRepository.save(transaction);
    }


    @Transactional
    public Transaction refund(Long transactionId) {

        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transação não encontrada"
                        ));

        Account account = accountRepository
                .findById(transaction.getAccount().getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Conta não encontrada"
                        ));

        transaction.transitionTo(TransactionStatus.REFUNDED);

        account.credit(transaction.getAmount());

        return transactionRepository.save(transaction);
    }
}