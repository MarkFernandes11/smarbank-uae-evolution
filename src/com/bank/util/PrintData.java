package com.bank.util;

import com.bank.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public class PrintData {

    public static void printError(String message) {
        System.out.println(String.format(IConstant.PRINT_ERROR, message));
    }

    public static void print(String message, BigDecimal amount) {
        System.out.println(String.format(message, amount));
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void displayMainMenu() {
        System.out.println("Main Menu");
        System.out.println("1. Create New Account");
        System.out.println("2. Login (Select Existing Account)");
        System.out.println("3. List All Accounts");
        System.out.println("4. Exit");
    }

    public static void displayWalletMenu() {
        System.out.println("Wallet Menu");
        System.out.println("1. Check your balance");
        System.out.println("2. Add money");
        System.out.println("3. Withdraw money");
        System.out.println("4. Transfer money");
        System.out.println("5. Display Transaction History");
        System.out.println("6. Main menu");
    }

    public static void printAccountHolders(List<String> accountHolders) {
        if (accountHolders.isEmpty()) {
            print(IConstant.ACCOUNTS_NOT_CREATED);
            return;
        }
        accountHolders.forEach(System.out::println);
    }

    public static void printTransactions(List<Transaction> transactionHistory) {
        if (transactionHistory.isEmpty()) {
            print(IConstant.NO_TRANSACTIONS);
        } else {
            transactionHistory.forEach(System.out::println);
        }
    }
}
