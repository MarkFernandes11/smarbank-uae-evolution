package com.bank.models;

import com.bank.exceptions.InsufficientBalanceException;
import com.bank.util.IConstant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountHolder;
    private double balance;
    private List<Transaction> txnHistory;

    /**
     * Constructor to initialize a account based on the name and starting amount
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     */
    public Account (final String accountHolder, final double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        txnHistory = new ArrayList<>();
        txnHistory.add(new Transaction(balance, "CREDITED", LocalDateTime.now()));
        // Need to generate a ID from account holder for transferring funds
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

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     * @param transfer Whether it is a transfer request or not
     */
    public void addMoney(double money, boolean transfer) {
        balance += money;
        if (!transfer) {
            txnHistory.add(new Transaction(money, "CREDITED", LocalDateTime.now()));
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     *
     * @param money amount to be withdrawn
     */
    public void withdrawMoney(double money, boolean transfer) throws InsufficientBalanceException {
        if ((balance - money) >= 0) {
            balance -= money;
            if (!transfer) {
                txnHistory.add(new Transaction(money, "DEBITED", LocalDateTime.now()));
            }
        } else {
            throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, balance));
        }
    }

    /**
     * Fetches the transaction history for the account holder
     */
    public List<Transaction> getTransactionHistory() {
        return txnHistory;
    }
}
