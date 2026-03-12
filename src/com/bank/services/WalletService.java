package com.bank.services;

import com.bank.exceptions.InsufficientBalanceException;
import com.bank.exceptions.SelfTransferException;
import com.bank.models.Transaction;
import com.bank.repositories.AccountRepository;
import com.bank.repositories.TransactionRepository;
import com.bank.util.DBConnection.PostgresConnection;
import com.bank.util.IConstant;
import com.bank.exceptions.AccountNotFoundException;
import com.bank.models.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class WalletService {
    private AccountRepository accountRepository = new AccountRepository();
    private TransactionRepository transactionRepository = new TransactionRepository();

    /**
     * Creates a new account with name and initial deposit and saves in db
     * @param name Name of account holder
     * @param amount Initial deposit
     * @return Returns the account created
     */
    public Account createAccount(final String name, final double amount) throws SQLException {
        Connection connection = null;
        try {
            connection = PostgresConnection.getConnection();
            connection.setAutoCommit(false);
            Account account = new Account(name, amount);
            accountRepository.saveAccount(connection, account);
            transactionRepository.saveTransaction(connection, account.getId(), Transaction.getTransaction(amount, "INITIAL CREDIT"));
            connection.commit();
            return account;
        } catch (Exception e) {
            rollbackTransaction(connection);
            throw e;
        } finally {
            closeConnection(connection);
        }
    }

    private void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Checks if account exists
     * @param name Name of account holder
     * @return if account exists it returns true else false
     */
    public boolean checkAccountExists(String name) throws SQLException {
        try (Connection connection = PostgresConnection.getConnection()) {
            return accountRepository.findByName(connection, name).isPresent();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Fetches the account detail based on the account name if exists
     * @param name Checks if account exists for the provided name
     * @return Returns the account found
     * @throws AccountNotFoundException Exception is thrown if account not found
     */
    public Account fetchAccount(final String name) throws SQLException, AccountNotFoundException {
        try (Connection connection = PostgresConnection.getConnection()) {
            return accountRepository.findByName(connection, name)
                    .orElseThrow(() -> new AccountNotFoundException(String.format(IConstant.ACCOUNT_NOT_FOUND, name)));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Fetches all the account holders
     * @return List of all account holder names is returned
     */
    public List<String> fetchAccountHolders() throws SQLException {
        try (Connection connection = PostgresConnection.getConnection()) {
            return accountRepository.getAllUsernames(connection);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Service to transfer funds from one account holder to another
     * @param account Source account holder
     * @param targetAccount Target account holder to transfer to
     * @param amount Amount to be transferred
     * @throws SelfTransferException Thrown when trying to transfer to self
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     */
    public void transferFunds(final Account account, final Account targetAccount, final double amount) throws SelfTransferException, SQLException, InsufficientBalanceException {
        Connection connection = null;
        try {
            connection = PostgresConnection.getConnection();
            connection.setAutoCommit(false);
            if (account.getAccountHolder().equals(targetAccount.getAccountHolder())) {
                throw new SelfTransferException(IConstant.SELF_TRANSFER_ERROR);
            }

            withdrawMoney(connection, amount, account.getId(), true);
            transactionRepository.saveTransaction(connection, account.getId(), Transaction.getTransaction(amount, "Sent to " + targetAccount.getAccountHolder()));

            addMoney(connection, amount, targetAccount.getId(), true);
            transactionRepository.saveTransaction(connection, targetAccount.getId(), Transaction.getTransaction(amount, "Received from " + account.getAccountHolder()));

            connection.commit();
        } catch (Exception e) {
            rollbackTransaction(connection);
            throw e;
        } finally {
            closeConnection(connection);
        }
    }

    public void addMoney(double money, int accountId, boolean transfer) throws SQLException {
        try (Connection connection = PostgresConnection.getConnection()) {
            addMoney(connection, money, accountId, transfer);
        }
    }

    /**
     * Adds money to the account if the amount is positive
     *
     * @param money the amount to be added
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     */
    public void addMoney(Connection connection, double money, int accountId, boolean transfer) {
        double currentBalance = accountRepository.getBalance(connection, accountId);
        currentBalance += money;
        accountRepository.updateBalance(connection, accountId, currentBalance);
        if (!transfer) {
            transactionRepository.saveTransaction(connection, accountId, Transaction.getTransaction(money, "CREDITED"));
        }
    }

    public void withdrawMoney(double money, int accountId, boolean transfer) throws SQLException, InsufficientBalanceException {
        try (Connection connection = PostgresConnection.getConnection()) {
            withdrawMoney(connection, money, accountId, transfer);
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     *
     * @param money amount to be withdrawn
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     */
    public void withdrawMoney(Connection connection, double money, int accountId, boolean transfer) throws InsufficientBalanceException {
        double currentBalance = accountRepository.getBalance(connection, accountId);
        if ((currentBalance - money) >= 0) {
            currentBalance -= money;
            accountRepository.updateBalance(connection, accountId, currentBalance);
            if (!transfer) {
                transactionRepository.saveTransaction(connection, accountId, Transaction.getTransaction(money, "DEBITED"));
            }
        } else {
            throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, currentBalance));
        }
    }

    /**
     * Fetches the balance from database
     * @param accountId accountId of the account holder
     * @return Returns the balance in the account
     */
    public double getAccountBalance(final int accountId) {
        try (Connection connection = PostgresConnection.getConnection()) {
            return accountRepository.getBalance(connection, accountId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Fetches the transaction history for the account holder
     * @param accountId accountId of the account holder
     * @return list of transactions of the account holder
     */
    public List<Transaction> getTransactionHistory(final int accountId) {
        try (Connection connection = PostgresConnection.getConnection()) {
            return transactionRepository.getTransactions(connection, accountId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
