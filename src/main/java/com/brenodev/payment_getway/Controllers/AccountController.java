package com.brenodev.payment_getway.Controllers;

import com.brenodev.payment_getway.DTOs.AccountDTO;
import com.brenodev.payment_getway.DTOs.AccountInfoDTO;
import com.brenodev.payment_getway.Entity.Account;
import com.brenodev.payment_getway.Repositories.AccountRepository;
import com.brenodev.payment_getway.Services.AccountService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @PostMapping
    public Account createAccount(@RequestBody AccountDTO account) {
        return accountService.createAccount(account);
    }

    @PatchMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/info/{id}")
    public AccountInfoDTO getAccountInfo(@PathVariable Long id) {

        return accountService.accountInfo(id);
    }






}
