package com.bank.models;

import java.time.LocalDateTime;

public record Transaction(double amount, String transactionType, LocalDateTime timeStamp) {

    public static Transaction getTransaction(final double amount, final String transactionType) {
        return new Transaction(amount, transactionType, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f AED", timeStamp, transactionType, amount);
    }

}
