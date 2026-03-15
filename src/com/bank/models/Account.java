package com.bank.models;

import java.math.BigDecimal;

public class Account {

    private int id;
    private String accountHolder;
    private BigDecimal balance;

    /**
     * Constructor to initialize an account based on the name and starting amount
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     */
    public Account (final String accountHolder, final BigDecimal balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    /**
     * Constructor to initialize an account based on the name, starting amount and id
     *
     * @param accountHolder Name of the account holder
     * @param balance Amount to be added to the balance
     * @param id id of the account holder
     */
    public Account (final String accountHolder, final BigDecimal balance, final int id) {
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Fetches the account holder name
     * @return returns name of the account holder
     */
    public String getAccountHolder() {
        return accountHolder;
    }

    /**
     * Fetches the balance from account object
     * @return Returns the balance in the account.
     */
    public BigDecimal getBalance() {
        return balance;
    }
}
