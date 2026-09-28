package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.AccountDTO;
import com.brenodev.payment_getway.DTOs.AccountInfoDTO;
import com.brenodev.payment_getway.Entity.Account;
import com.brenodev.payment_getway.Exception.ResourceNotFoundException;
import com.brenodev.payment_getway.Repositories.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;


    public Account createAccount(AccountDTO dto) {
        if(dto.perTransactionLimit() != null && dto.dailyLimit() != null && dto.perTransactionLimit().compareTo(dto.dailyLimit()) > 0) {
            throw new IllegalArgumentException("O limite por transação não pode ser maior que o limite diário");
        }

        Account account = new Account(
                dto.balance(),
                dto.perTransactionLimit(),
                dto.dailyLimit()
        );


        return  accountRepository.save(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Conta não encontrada"));
    }

    public AccountInfoDTO accountInfo( Long id) {


        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conta não encontrada"
                ));

        return new AccountInfoDTO(
                account.getId(),
                account.getBalance(),
                account.getDailyLimit(),
                account.getPerTransactionLimit()
        );
    }

    public Account updateAccount(Long id, Account account ) {

        if(id.equals(account.getId()) == false) {
            throw new RuntimeException("Id não localizado");
        }

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.findById(id);

        if(accountRepository.findById(id).isPresent()) {
            accountRepository.deleteById(id);
        } else {
            throw new RuntimeException("Conta não pode ser excluída");
        }
    }

}
