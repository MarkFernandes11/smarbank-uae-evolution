package com.bank.api.dto;

import com.bank.api.enums.AccountType;

import java.math.BigDecimal;

public record AccountRequest(String accountHolder, BigDecimal balance, AccountType accountType) {

    @Override
    public String toString() {
        return String.format("%s has %.2f AED", accountHolder, balance);
    }

}