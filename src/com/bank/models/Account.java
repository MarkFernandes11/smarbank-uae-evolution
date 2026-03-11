package com.bank.models;

import com.bank.exceptions.InsufficientBalanceException;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import com.bank.util.IConstant;

import java.util.List;

public class Account {

    private int id;
    private String accountHolder;
    private double balance;

    private TransactionRepository transactionRepository = new TransactionRepository();

    private AccountRepository accountRepository = new AccountRepository();

    /**
     * Constructor to initialize an account based on the name and starting amount
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     */
    public Account (final String accountHolder, final double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    /**
     * Constructor to initialize an account based on the name, starting amount and id
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     * @param id id of the account holder
     */
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
     * Fetches the balance from account object
     * @return Returns the balance in the account.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Fetches the balance from database
     * @param accountId accountId of the account holder
     * @return Returns the balance in the account
     */
    public double getAccountBalance(final int accountId) {
        return accountRepository.getBalance(accountId);
    }

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     * @param accountId accountId of the account holder
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
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
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
     * @param accountId accountId of the account holder
     * @return list of transactions of the account holder
     */
    public List<Transaction> getTransactionHistory(final int accountId) {
        return transactionRepository.getTransactions(accountId);
    }
}
