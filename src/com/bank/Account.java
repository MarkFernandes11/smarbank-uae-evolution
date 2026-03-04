package com.bank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountHolder;
    private double balance;
    private List<String> txnHistory;

    public Account (final String accountHolder, final double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        txnHistory = new ArrayList<>();
        txnHistory.add(balance + " credited on " + LocalDateTime.now());
    }

    public void getBalance() {
        System.out.println("You have " + balance + " in your account.");
    }

    public void addMoney(double money) {
        if (money > 0) {
            balance += money;
            System.out.println(money + " added to your account.");
            txnHistory.add(money + " credited on " + LocalDateTime.now());
        } else {
            System.out.println("Invalid input");
        }
    }

    public void withdrawMoney(double money) {
        if (money > 0) {
            if (balance - money > 0) {
                balance -= money;
                System.out.println(money + " withdrawn from your account.");
                txnHistory.add(money + " withdrawn on " + LocalDateTime.now());
            } else {
                System.out.println("You do not have sufficient funds to withdraw.");
            }
        } else {
            System.out.println("Invalid input");
        }
    }

    public void getTransactionHistory() {
        if (txnHistory.isEmpty()) {
            System.out.println("No transactions to be displayed");
        } else {
            txnHistory.forEach(System.out::println);
        }
    }
}
