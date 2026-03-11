package com.bank.models;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import com.bank.util.IConstant;

import java.util.List;
import java.util.Optional;

public class Account {

    private int id;
    private String accountHolder;
    private double balance;

    private TransactionRepository transactionRepository = new TransactionRepository();

    private AccountRepository accountRepository = new AccountRepository();

    /**
     * Constructor to initialize a account based on the name and starting amount
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     */
    public Account (final String accountHolder, final double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    public Account (final String accountHolder, final double balance, final int id) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Fetches the account holder name
     * @return returns name of the account holder
     */
    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Returns the balance in the account.
     */
    public double getBalance() {
        return balance;
    }

    // TODOD : Need to fix get balance to avoid issues in add and withdraw-
    public double getAccountBalance(final int accountId) {
        return accountRepository.getBalance(accountId);
    }

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     * @param transfer Whether it is a transfer request or not
     */
    public void addMoney(double money, int accountId, boolean transfer) {
        double currentBalance = accountRepository.getBalance(accountId);
        currentBalance += money;
        accountRepository.updateBalance(accountId, currentBalance);
        if (!transfer) {
            transactionRepository.saveTransaction(accountId, Transaction.getTransaction(money, "CREDITED"));
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     *
     * @param money amount to be withdrawn
     */
    public void withdrawMoney(double money, int accountId, boolean transfer) throws InsufficientBalanceException {
        double currentBalance = accountRepository.getBalance(accountId);
        if ((currentBalance - money) >= 0) {
            currentBalance -= money;
            accountRepository.updateBalance(accountId, currentBalance);
            if (!transfer) {
                transactionRepository.saveTransaction(accountId, Transaction.getTransaction(money, "DEBITED"));
            }
        } else {
            throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, balance));
        }
    }

    /**
     * Fetches the transaction history for the account holder
     */
    public List<Transaction> getTransactionHistory(final int accountId) {
        return transactionRepository.getTransactions(accountId);
    }
}
