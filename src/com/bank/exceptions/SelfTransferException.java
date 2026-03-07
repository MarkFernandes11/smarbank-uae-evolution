package com.bank.exceptions;

public class SelfTransferException extends BankingException {
    public SelfTransferException(String message) {
        super(message);
    }
}
