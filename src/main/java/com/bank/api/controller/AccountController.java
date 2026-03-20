package com.bank.api.controller;

import com.bank.api.dto.AccountRequest;
import com.bank.api.dto.AccountResponse;
import com.bank.api.exception.AccountAlreadyExistsException;
import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {

    private WalletService walletService;

    @Autowired
    public AccountController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "/create")
    public AccountResponse saveAccount(@Valid @RequestBody AccountRequest request) throws AccountAlreadyExistsException {
        return walletService.createAccount(request);
    }

    @GetMapping(value = "/login/{accountId}")
    public AccountResponse login(@PathVariable Long accountId) throws AccountNotFoundException {
        return walletService.fetchAccount(accountId);
    }

    @GetMapping(value = "/account-holders")
    public List<String> getAllAccountHolders() {
        return walletService.fetchAccountHolders();
    }

    @GetMapping(value = "/balance/{accountId}")
    public BigDecimal getBalance(@PathVariable Long accountId) throws AccountNotFoundException {
        return walletService.getAccountBalance(accountId);
    }

    @DeleteMapping(value = "/{accountId}")
    public void closeAccount(@PathVariable Long accountId) {
        walletService.closeAccount(accountId);
    }
}
