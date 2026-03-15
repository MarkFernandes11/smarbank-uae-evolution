package com.bank.api.dto;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public record TransactionDTO(BigDecimal amount, String transactionType, LocalDateTime timeStamp) {

    public static TransactionDTO getTransaction(final BigDecimal amount, final String transactionType) {
        return new TransactionDTO(amount, transactionType, LocalDateTime.now());
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f AED", timeStamp, transactionType, amount);
    }

}
