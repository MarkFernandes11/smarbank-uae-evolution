package com.bank.api.controller;

import com.bank.api.dto.TransactionDTO;
import com.bank.api.dto.TransferFundsRequest;
import com.bank.api.dto.TransferRequest;
import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.NegativeOrZeroAmountException;
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

    @PostMapping(value = "/transfer/add-funds")
    public BigDecimal addFunds(@RequestBody TransferRequest transferRequest) throws AccountNotFoundException, NegativeOrZeroAmountException {
        return walletService.addMoney(transferRequest, "CREDITED");
    }

    @PostMapping(value = "/transfer/withdraw-funds")
    public BigDecimal withdrawFunds(@RequestBody TransferRequest request) throws InsufficientBalanceException, AccountNotFoundException, NegativeOrZeroAmountException {
        return walletService.withdrawMoney(request, "DEBITED");
    }

    @PostMapping(value = "/transfer/transfer-funds")
    public BigDecimal transferFunds(@RequestBody TransferFundsRequest request) throws InsufficientBalanceException, SelfTransferException, AccountNotFoundException, NegativeOrZeroAmountException {
        return walletService.transferFunds(request);
    }

    @GetMapping(value = "/transactions/{accountId}")
    public List<TransactionDTO> getTransactionHistory(@PathVariable("accountId") final Long accountId) {
        return walletService.getTransactionHistory(accountId);
    }
}
