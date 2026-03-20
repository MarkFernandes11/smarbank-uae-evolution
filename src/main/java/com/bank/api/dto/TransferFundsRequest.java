package com.bank.api.dto;

import java.math.BigDecimal;

public record TransferFundsRequest(BigDecimal amount, Long accountId, Long targetAccountId) { }
