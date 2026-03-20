package com.bank.api.dto;

import com.bank.api.enums.AccountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AccountRequest(
        @NotBlank(message = "Account holder name cannot be blank")
        @Size(min = 2, max = 50, message = "Name should be between 2 and 50 characters")
        String accountHolder,
        @NotNull(message = "Balance cannot be null")
        @PositiveOrZero(message = "Amount should be zero or greater")
        BigDecimal balance,
        @NotNull(message = "Account type is required")
        AccountType accountType) {

    @Override
    public String toString() {
        return String.format("%s has %.2f AED", accountHolder, balance);
    }

}