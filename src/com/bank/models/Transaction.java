package com.bank.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(BigDecimal amount, String transactionType, LocalDateTime timeStamp) {

    public static Transaction getTransaction(final BigDecimal amount, final String transactionType) {
        return new Transaction(amount, transactionType, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f AED", timeStamp, transactionType, amount);
    }

}
