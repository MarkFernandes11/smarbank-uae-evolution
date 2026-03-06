package com.bank;

import com.bank.exceptions.InsufficientBalanceException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountHolder;
    private double balance;
    private List<String> txnHistory;

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
        txnHistory.add(balance + " credited on " + LocalDateTime.now());
    }

    /**
     * Fetches the balance in the account.
     */
    public void getBalance() {
        System.out.println("You have " + balance + " in your account.");
    }

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     */
    public void addMoney(double money) {
        if (money > 0) {
            balance += money;
            System.out.println(money + " added to your account.");
            txnHistory.add(money + " credited on " + LocalDateTime.now());
        } else {
            System.out.println("Invalid input");
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     *
     * @param money amount to be withdrawn
     */
    public void withdrawMoney(double money) throws InsufficientBalanceException {
        if (money > 0) {
            if (balance - money > 0) {
                balance -= money;
                System.out.println(money + " withdrawn from your account.");
                txnHistory.add(money + " withdrawn on " + LocalDateTime.now());
            } else {
                throw new InsufficientBalanceException("Insufficient balance in your account. Available balance is " + balance);
            }
        } else {
            System.out.println("Invalid input");
        }
    }

    /**
     * Fetches the transaction history for the account holder
     */
    public void getTransactionHistory() {
        if (txnHistory.isEmpty()) {
            System.out.println("No transactions to be displayed");
        } else {
            txnHistory.forEach(System.out::println);
        }
    }
}
