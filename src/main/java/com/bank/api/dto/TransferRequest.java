package com.bank.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferRequest(
        @NotNull(message = "Balance cannot be null")
        @Positive(message = "Amount should be positive")
        BigDecimal amount,
        @NotNull Long accountId) { }
