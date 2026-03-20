package com.bank.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferRequest(BigDecimal amount, Long accountId) { }
