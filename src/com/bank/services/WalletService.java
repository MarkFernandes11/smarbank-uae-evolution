package com.bank.services;

import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.SelfTransferException;
import com.bank.models.Transaction;
import com.bank.util.IConstant;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.models.Account;

import java.time.LocalDateTime;
import java.util.*;

public class WalletService {
    private static final Map<String, Account> ACCOUNTS = new HashMap<>();

    /**
     * Creates a new account with name and initial deposit
     * @param name Name of account holder
     * @param amount Initial deposit
     * @return Returns the account created
     */
    public Account createAccount(final String name, final double amount) {
        Account account = new Account(name, amount);
        ACCOUNTS.put(name, account);
        return account;
    }

    /**
     * Checks if account exists
     * @param name Name of account holder
     * @return if account exists it returns true else false
     */
    public boolean checkAccountExists(String name) {
        return ACCOUNTS.containsKey(name);
    }

    /**
     * Fetches the account detail based on the account name if exists
     * @param name Checks if account exists for the provided name
     * @return Returns the account found
     * @throws AccountNotFoundException Exception is thrown if account not found
     */
    public Account fetchAccount(final String name) throws AccountNotFoundException {
        return Optional.ofNullable(ACCOUNTS.get(name))
                       .orElseThrow(() -> new AccountNotFoundException(String.format(IConstant.ACCOUNT_NOT_FOUND, name)));
    }

    /**
     * Fetches all the account holders
     * @return Set of all account holders is returned
     */
    public Set<String> fetchAccountHolders() {
        return ACCOUNTS.keySet();
    }

    /**
     * Service to transfer funds from one account holder to another
     * @param account Source account holder
     * @param targetAccount Target account holder to transfer to
     * @param amount Amount to be transferred
     * @throws SelfTransferException Thrown when trying to transfer to self
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     */
    public void transferFunds(final Account account, final Account targetAccount, final double amount) throws SelfTransferException, InsufficientBalanceException {
        if (account.getAccountHolder().equals(targetAccount.getAccountHolder())) {
            throw new SelfTransferException(IConstant.SELF_TRANSFER_ERROR);
        }

        account.withdrawMoney(amount, true);
        account.getTransactionHistory().add(new Transaction(amount, "Sent to " + targetAccount.getAccountHolder(), LocalDateTime.now()));

        targetAccount.addMoney(amount, true);
        targetAccount.getTransactionHistory().add(new Transaction(amount, "Received from " + account.getAccountHolder(), LocalDateTime.now()));
    }
}
