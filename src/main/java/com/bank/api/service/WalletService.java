package main.java.com.bank.api.service;

import main.java.com.bank.api.exception.InsufficientBalanceException;
import main.java.com.bank.api.exception.SelfTransferException;
import main.java.com.bank.api.model.Transaction;
import main.java.com.bank.api.repository.AccountRepository;
import main.java.com.bank.api.repository.TransactionRepository;
import main.java.com.bank.api.util.DBConnection.PostgresConnection;
import main.java.com.bank.api.util.IConstant;
import main.java.com.bank.api.exception.AccountNotFoundException;
import main.java.com.bank.api.model.Account;

import java.math.BigDecimal;
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
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    public Account createAccount(final String name, final BigDecimal amount) throws SQLException {
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

    /**
     * Rolls back the transaction in case of something went wrong
     * @param connection Connection to communicate with the db
     */
    private void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Closes the connection once the interaction with db is over
     * @param connection Connection to communicate with the db
     */
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
     * @throws SQLException Thrown exception when some issue in interacting with db
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
     * @throws SQLException Thrown exception when some issue in interacting with db
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
     * @throws SQLException Thrown exception when some issue in interacting with db
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
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    public void transferFunds(final Account account, final Account targetAccount, final BigDecimal amount) throws SelfTransferException, SQLException, InsufficientBalanceException {
        Connection connection = null;
        try {
            connection = PostgresConnection.getConnection();
            connection.setAutoCommit(false);
            if (account.getAccountHolder().equals(targetAccount.getAccountHolder())) {
                throw new SelfTransferException(IConstant.SELF_TRANSFER_ERROR);
            }

            withdrawMoney(Optional.of(connection), amount, account.getId(), true);
            transactionRepository.saveTransaction(connection, account.getId(), Transaction.getTransaction(amount, "Sent to " + targetAccount.getAccountHolder()));

            addMoney(Optional.of(connection), amount, targetAccount.getId(), true);
            transactionRepository.saveTransaction(connection, targetAccount.getId(), Transaction.getTransaction(amount, "Received from " + account.getAccountHolder()));

            connection.commit();
        } catch (Exception e) {
            rollbackTransaction(connection);
            throw e;
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * Adds money to the account if the amount is positive
     * @param connectionOpt Optional Connection to communicate with the db
     * @param money the amount to be added
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    public void addMoney(Optional<Connection> connectionOpt, BigDecimal money, int accountId, boolean transfer) throws SQLException {
        Connection connection = null;
        try {
            connection = connectionOpt.isPresent() ? connectionOpt.get() : PostgresConnection.getConnection();
            connection.setAutoCommit(false);

            BigDecimal currentBalance = accountRepository.getBalance(connection, accountId);
            currentBalance = currentBalance.add(money);
            accountRepository.updateBalance(connection, accountId, currentBalance);
            if (!transfer) {
                transactionRepository.saveTransaction(connection, accountId, Transaction.getTransaction(money, "CREDITED"));
            }
            if (!transfer) connection.commit();
        } catch (Exception e) {
            if (!connectionOpt.isPresent()) {
                rollbackTransaction(connection);
            }
            throw e;
        } finally {
            if (!connectionOpt.isPresent()) {
                closeConnection(connection);
            }
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     * @param connectionOpt Optional Connection to communicate with the db
     * @param money amount to be withdrawn
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    public void withdrawMoney(Optional<Connection> connectionOpt, BigDecimal money, int accountId, boolean transfer) throws InsufficientBalanceException, SQLException {
        Connection connection = null;
        boolean insufficientBalance = false;
        try {
            connection = connectionOpt.isPresent() ? connectionOpt.get() : PostgresConnection.getConnection();
            connection.setAutoCommit(false);
            BigDecimal currentBalance = accountRepository.getBalance(connection, accountId);
            if (!(currentBalance.subtract(money).compareTo(BigDecimal.ZERO) < 0)) {
                currentBalance = currentBalance.subtract(money);
                accountRepository.updateBalance(connection, accountId, currentBalance);
                if (!transfer) {
                    transactionRepository.saveTransaction(connection, accountId, Transaction.getTransaction(money, "DEBITED"));
                }
                if (!transfer) connection.commit();
            } else {
                insufficientBalance = true;
                throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, currentBalance));
            }
        } catch (Exception e) {
            if (!connectionOpt.isPresent() && !insufficientBalance) {
                rollbackTransaction(connection);
            }
            throw e;
        } finally {
            if (!connectionOpt.isPresent()) {
                closeConnection(connection);
            }
        }
    }

    /**
     * Fetches the balance from database
     * @param accountId accountId of the account holder
     * @return Returns the balance in the account
     */
    public BigDecimal getAccountBalance(final int accountId) {
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
