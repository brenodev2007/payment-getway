package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionDTO;
import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import com.brenodev.payment_getway.Entity.Account;
import com.brenodev.payment_getway.Entity.IdempotencyKey;
import com.brenodev.payment_getway.Entity.Merchant;
import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Enums.DeclineReason;
import com.brenodev.payment_getway.Enums.IdempotencyStatus;
import com.brenodev.payment_getway.Enums.TransactionStatus;
import com.brenodev.payment_getway.Repositories.AccountRepository;
import com.brenodev.payment_getway.Repositories.IdempotencyRepository;
import com.brenodev.payment_getway.Repositories.MerchantRepository;
import com.brenodev.payment_getway.Repositories.TransactionRepository;
import com.brenodev.payment_getway.Exception.ResourceNotFoundException;
import com.brenodev.payment_getway.webhook.TransactionStatusChangedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MerchantRepository merchantRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final IdempotencyRepository idempotencyRepository;


    private final OutboxService outboxService;


    @Transactional
    public Transaction create(TransactionDTO dto, String idempotencyKey) {

        String requestHash = generateHash(dto);

        Optional<IdempotencyKey> existing =
                idempotencyRepository.findByKey(idempotencyKey);

        if (existing.isPresent()) {

            IdempotencyKey savedKey = existing.get();

            if (!savedKey.getRequestHash().equals(requestHash)) {
                throw new IllegalArgumentException(
                        "Chave de idempotência utilizada com dados diferentes"
                );
            }

            if (savedKey.getTransaction() != null) {
                return savedKey.getTransaction();
            }

            throw new IllegalArgumentException(
                    "A transação está sendo processada"
            );
        }

        // 1. Busca a conta
        Account account = accountRepository
                .findById(dto.accountId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Conta não encontrada"));

        // 2. Busca o merchant
        Merchant merchant = merchantRepository
                .findById(dto.merchantId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Merchant não encontrado"));

        // 3. Cria a chave de idempotência
        IdempotencyKey key = new IdempotencyKey();

        key.setKey(idempotencyKey);
        key.setRequestHash(requestHash);
        key.setStatus(IdempotencyStatus.PROCESSING);
        key.setCreatedAt(LocalDateTime.now());

        // IMPORTANTE
        key.setAccount(account);

        // Agora sim salva
        idempotencyRepository.saveAndFlush(key);

        // 4. Cria a transação
        Transaction transaction = Transaction.create(
                account,
                merchant,
                dto.amount()
        );

        transaction.transitionTo(TransactionStatus.PROCESSING);

        // 5. Avalia a transação
        DeclineReason reason = evaluate(account, dto.amount());

        if (reason != null) {

            transaction.decline(reason);

        } else {

            account.debit(dto.amount());

            transaction.transitionTo(TransactionStatus.APPROVED);
        }

        // 6. Salva a transação
        Transaction savedTransaction =
                transactionRepository.save(transaction);

        TransactionWebhookDTO payload =
                new TransactionWebhookDTO(
                        savedTransaction.getId(),
                        savedTransaction.getMerchant().getId(),
                        savedTransaction.getAmount(),
                        savedTransaction.getStatus(),
                        savedTransaction.getCreatedAt(),
                        Instant.now()
                );

        String eventType = switch (savedTransaction.getStatus()) {
            case APPROVED -> "transaction.approved";
            case DECLINED -> "transaction.declined";
            case REFUNDED -> "transaction.refunded";
            default -> "transaction.status_changed";
        };

        outboxService.createEvent(
                eventType,
                savedTransaction.getId(),
                savedTransaction.getMerchant().getWebhookUrl(),
                payload
        );


        // 7. Finaliza a idempotência
        key.setTransaction(savedTransaction);
        key.setStatus(IdempotencyStatus.COMPLETED);

        idempotencyRepository.save(key);



        return savedTransaction;
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


    @Transactional
    private DeclineReason evaluate(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            return DeclineReason.INSUFFICIENT_FUNDS;
        }

        if (amount.compareTo(account.getPerTransactionLimit()) > 0) {
            return DeclineReason.PER_TRANSACTION_LIMIT_EXCEEDED;
        }

        return null;
    }


    private String generateHash(TransactionDTO dto) {
        try {
            String data = dto.accountId() + ":" +
                    dto.merchantId() + ":" +
                    dto.amount().toPlainString();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    data.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Erro ao gerar hash", e);
        }
    }
}