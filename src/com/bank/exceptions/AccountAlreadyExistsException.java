package com.bank.exceptions;

public class AccountAlreadyExistsException extends BankingException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}
