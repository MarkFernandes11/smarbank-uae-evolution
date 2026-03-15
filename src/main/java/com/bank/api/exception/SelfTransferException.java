package main.java.com.bank.api.exception;

public class SelfTransferException extends BankingException {
    public SelfTransferException(String message) {
        super(message);
    }
}
