package com.bank.api.controller;

import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController(value = "account")
public class AccountController {

    private WalletService walletService;

    @Autowired
    public AccountController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value = "create")
    public void saveAccount(String name, BigDecimal amount) throws SQLException {
        walletService.createAccount(name, amount);
    }

    @GetMapping(value = "login")
    public void login(@RequestParam String name) throws AccountNotFoundException {
        walletService.fetchAccount(name);
    }

    @GetMapping()
    public List<String> getAllAccountHolders() {
        return walletService.fetchAccountHolders();
    }

    @GetMapping(value = "balance")
    public BigDecimal getBalance(@RequestParam Long accountId) {
        return walletService.getAccountBalance(accountId);
    }


}
