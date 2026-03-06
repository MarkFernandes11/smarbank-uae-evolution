package com.bank.services;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.models.Account;

import java.util.*;

public class WalletService {
    private static final Map<String, Account> ACCOUNTS = new HashMap<>();

    public Account createAccount(final String name, final double amount) {
        Account account = new Account(name, amount);
        ACCOUNTS.put(name, account);
        return account;
    }

    public boolean checkAccountExists(String name) {
        return ACCOUNTS.containsKey(name);
    }

    public Account fetchAccount(final String name) throws AccountNotFoundException {
        return Optional.ofNullable(ACCOUNTS.get(name))
                       .orElseThrow(() -> new AccountNotFoundException("Account not found with name " + name));
    }

    public void fetchAccountHolders() {
        if (ACCOUNTS.isEmpty()) {
            System.out.println("No accounts created.");
            return;
        }
        ACCOUNTS.keySet().forEach(System.out::println);
    }
}
