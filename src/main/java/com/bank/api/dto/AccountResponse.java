package com.bank.api.dto;

import com.bank.api.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(Long id, String accountHolder, BigDecimal balance, AccountType accountType,
                              boolean isActive, LocalDateTime createdAt) {

    @Override
    public String toString() {
        return String.format("%s has %.2f AED", accountHolder, balance);
    }

}