package com.bank.api.service;

import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.SelfTransferException;
import com.bank.api.dto.TransactionDTO;
import com.bank.api.model.Transaction;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.util.IConstant;
import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WalletService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public WalletService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Creates a new account with name and initial deposit and saves in db
     * @param name Name of account holder
     * @param amount Initial deposit
     * @return Returns the account created
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional
    public Account createAccount(final String name, final BigDecimal amount) throws SQLException {
        try {
            Account.AccountBuilder accountBuilder = new Account.AccountBuilder().builder().withAccountHolder(name).withBalance(amount);
            Account account = new Account(accountBuilder);
            account = accountRepository.saveAndFlush(account);
            transactionRepository.save(getTransaction(account, "INITIAL CREDIT", amount));
            return account;
        } catch (Exception e) {
            throw e;
        }
    }

    private Transaction getTransaction(Account account, String transactionType, BigDecimal amount) {
        Transaction transaction = new Transaction.TransactionBuilder().builder()
                .withTransactionType(transactionType)
                .withAccount(account)
                .withTimestamp(LocalDateTime.now())
                .withAmount(amount)
                .build();
        return transaction;
    }

    /**
     * Checks if account exists
     * @param name Name of account holder
     * @return if account exists it returns true else false
     */
    public boolean checkAccountExists(String name) {
        return accountRepository.findByAccountHolder(name).isPresent();
    }

    /**
     * Fetches the account detail based on the account name if exists
     * @param name Checks if account exists for the provided name
     * @return Returns the account found
     * @throws AccountNotFoundException Exception is thrown if account not found
     */
    public Account fetchAccount(final String name) throws AccountNotFoundException {
        return accountRepository.findByAccountHolder(name)
                .orElseThrow(() -> new AccountNotFoundException(String.format(IConstant.ACCOUNT_NOT_FOUND, name)));
    }

    /**
     * Fetches all the account holders
     * @return List of all account holder names is returned
     */
    public List<String> fetchAccountHolders() {
        return accountRepository.getAllAccountHolders();
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
    @Transactional
    public void transferFunds(final Account account, final Account targetAccount, final BigDecimal amount) throws SelfTransferException, SQLException, InsufficientBalanceException {
        try {
            if (account.getAccountHolder().equals(targetAccount.getAccountHolder())) {
                throw new SelfTransferException(IConstant.SELF_TRANSFER_ERROR);
            }

            withdrawMoney(amount, account.getId(), true);
            transactionRepository.save(getTransaction(account, "Sent to " + targetAccount.getAccountHolder(), amount));

            addMoney(amount, targetAccount.getId(), true);
            transactionRepository.save(getTransaction(targetAccount, "Received from " + account.getAccountHolder(), amount));
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Adds money to the account if the amount is positive
     * @param money the amount to be added
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional
    public void addMoney(BigDecimal money, Long accountId, boolean transfer) {
        try {
            BigDecimal currentBalance = accountRepository.getBalanceById(accountId);
            currentBalance = currentBalance.add(money);
            accountRepository.updateBalance(accountId, currentBalance);
            if (!transfer) {
                // TODO need to fix this account null value
                transactionRepository.save(getTransaction(null, "CREDITED", money));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Withdraws money from the account if withdrawal possible
     * @param money amount to be withdrawn
     * @param accountId accountId of the account holder
     * @param transfer Whether it is a transfer request or not
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional
    public void withdrawMoney(BigDecimal money, Long accountId, boolean transfer) throws InsufficientBalanceException {
        try {
            BigDecimal currentBalance = accountRepository.getBalanceById(accountId);
            if (!(currentBalance.subtract(money).compareTo(BigDecimal.ZERO) < 0)) {
                currentBalance = currentBalance.subtract(money);
                accountRepository.updateBalance(accountId, currentBalance);
                if (!transfer) {
                    // TODO need to fix this account null value
                    transactionRepository.save(null);
//                    transactionRepository.saveTransaction(connection, accountId, TransactionDTO.getTransaction(money, "DEBITED"));
                }
            } else {
                throw new InsufficientBalanceException(String.format(IConstant.INSUFFICIENT_BALANCE, currentBalance));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Fetches the balance from database
     * @param accountId accountId of the account holder
     * @return Returns the balance in the account
     */
    public BigDecimal getAccountBalance(final Long accountId) {
        return accountRepository.getBalanceById(accountId);
    }

    /**
     * Fetches the transaction history for the account holder
     * @param accountId accountId of the account holder
     * @return list of transactions of the account holder
     */
    public List<TransactionDTO> getTransactionHistory(final Long accountId) {
        List<Transaction> transactions = transactionRepository.findAllTransactionByAccountId(accountId);
        return transactions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        return new TransactionDTO(transaction.getAmount(), transaction.getTransactionType(), transaction.getTimeStamp());
    }
}
