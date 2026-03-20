package com.bank.api.controller;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.SelfTransferException;
import com.bank.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class TransactionController {

    private WalletService walletService;

    @Autowired
    public TransactionController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(value = "/transfer/add-funds")
    public void addFunds(@RequestParam BigDecimal money, Long accountId) throws AccountNotFoundException {
        walletService.addMoney(money, accountId, false);
    }

    @GetMapping(value = "/transfer/withdraw-funds")
    public void withdrawFunds(@RequestParam BigDecimal money, Long accountId) throws InsufficientBalanceException, AccountNotFoundException {
        walletService.withdrawMoney(money, accountId, false);
    }

    @GetMapping(value = "/transfer/transfer-funds")
    public void transferFunds(@RequestParam BigDecimal money, Long accountId) throws InsufficientBalanceException, SelfTransferException, AccountNotFoundException {
        walletService.transferFunds(null, null, money);
    }

    @GetMapping(value = "/transactions/{accountId}")
    public List<TransactionDTO> getTransactionHistory(@PathVariable("accountId") final Long accountId) {
        return walletService.getTransactionHistory(accountId);
    }
}
