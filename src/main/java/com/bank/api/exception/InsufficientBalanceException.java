package main.java.com.bank.api.exception;

public class InsufficientBalanceException extends BankingException {
    public InsufficientBalanceException(String message) {
        super(message);
    }
}
