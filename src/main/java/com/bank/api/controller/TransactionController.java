package com.bank.api.controller;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.SelfTransferException;
import com.bank.api.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@RestController
public class TransactionController {

    private WalletService walletService;

    @Autowired
    public TransactionController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(value = "transfer/add-funds")
    public void addFunds(@RequestParam BigDecimal money, Long accountId) {
        walletService.addMoney(money, accountId, false);
    }

    @GetMapping(value = "transfer/withdraw-funds")
    public void withdrawFunds(@RequestParam BigDecimal money, Long accountId) throws InsufficientBalanceException {
        walletService.withdrawMoney(money, accountId, false);
    }

    @GetMapping(value = "transfer/transfer-funds")
    public void transferFunds(@RequestParam BigDecimal money, Long accountId) throws InsufficientBalanceException, SelfTransferException, SQLException {
        walletService.transferFunds(null, null, money);
    }

    @GetMapping(value = "transactions/{accountId}")
    public List<TransactionDTO> getTransactionHistory(@PathVariable("accountId") final Long accountId) {
        return walletService.getTransactionHistory(accountId);
    }
}
