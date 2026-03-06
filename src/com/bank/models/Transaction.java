package com.bank.models;

import java.time.LocalDateTime;

public record Transaction(double amount, String transactionType, LocalDateTime timeStamp) {

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f AED", timeStamp, transactionType, amount);
    }

}
