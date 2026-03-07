package com.bank.models;

import com.bank.exceptions.AccountNotFoundException;
import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.SelfTransferException;
import com.bank.services.WalletService;
import com.bank.util.IConstant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private String accountHolder;
    private double balance;
    private List<Transaction> txnHistory;

    private WalletService service = new WalletService();

    /**
     * Constructor to initialize a account based on the name and starting amount
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     */
    public Account (final String accountHolder, final double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        txnHistory = new ArrayList<>();
        txnHistory.add(new Transaction(balance, "CREDITED", LocalDateTime.now()));
        // Need to generate a ID from account holder for transferring funds
    }

    /**
     * Fetches the account holder name
     * @return returns name of the account holder
     */
    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Returns the balance in the account.
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     */
    public void addMoney(double money, boolean transfer) {
        balance += money;
        txnHistory.add(new Transaction(money, "CREDITED", LocalDateTime.now()));
    }

    /**
     * Withdraws money from the account if withdrawal possible
     *
     * @param money amount to be withdrawn
     */
    public void withdrawMoney(double money, boolean transfer) throws InsufficientBalanceException {
        if ((balance - money) >= 0) {
            balance -= money;
            if (!transfer) {
                txnHistory.add(new Transaction(money, "DEBITED", LocalDateTime.now()));
            }
        } else {
            throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, balance));
        }
    }

    public void transferFunds(final Account account, final String transferTo, final double amount) throws AccountNotFoundException,
            SelfTransferException, InsufficientBalanceException {
        // Need to implement this method
        // 1. check if user exists if not throw error
        Account targetAccount = service.fetchAccount(transferTo);
        // 2. If user exists then check if same user or not
        if (account.getAccountHolder().equals(targetAccount.getAccountHolder())) {
            throw new SelfTransferException("Self transfer not supported");
        }
        // 3. If all well then withdraw from source account and check if withdrawal is valid or not
        withdrawMoney(amount, true);
        // 4. If withdrawal valid from source then deposit to target account
        targetAccount.addMoney(amount, true);
        // 5. Need to update transactions history with new message instead of normal credit debit message
        account.getTransactionHistory().add(new Transaction(amount, "Sent to " + transferTo, LocalDateTime.now()));
        targetAccount.getTransactionHistory().add(new Transaction(amount, "Received from " + account.getAccountHolder(), LocalDateTime.now()));
    }

    /**
     * Fetches the transaction history for the account holder
     */
    public List<Transaction> getTransactionHistory() {
        return txnHistory;
    }

    public void printTransactionHistory() {
        if (txnHistory.isEmpty()) {
            System.out.println("No transactions to be displayed");
        } else {
            txnHistory.forEach(System.out::println);
        }
    }
}
