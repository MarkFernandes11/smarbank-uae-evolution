package com.bank.api.exception;

public class NegativeOrZeroAmountException extends BankingException {
    public NegativeOrZeroAmountException(String message) {
        super(message);
    }
}
