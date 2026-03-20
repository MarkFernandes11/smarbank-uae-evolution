package com.bank.api.service;

import com.bank.api.dto.*;
import com.bank.api.enums.AccountType;
import com.bank.api.exception.AccountAlreadyExistsException;
import com.bank.api.exception.InsufficientBalanceException;
import com.bank.api.exception.SelfTransferException;
import com.bank.api.model.Transaction;
import com.bank.api.repository.AccountRepository;
import com.bank.api.repository.TransactionRepository;
import com.bank.api.util.IConstant;
import com.bank.api.exception.AccountNotFoundException;
import com.bank.api.model.Account;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
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
     * @param request Consistin of name and initial deposit of account holder
     * @return Returns the account created
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional(rollbackFor = Exception.class)
    public AccountResponse createAccount(final AccountRequest request) throws AccountAlreadyExistsException {
        try {
            String name = request.accountHolder();
            BigDecimal amount = request.balance();
            AccountType type = Optional.ofNullable(request.accountType()).orElse(AccountType.SAVINGS);

            if (checkAccountExists(name)) {
                throw new AccountAlreadyExistsException(String.format(IConstant.ACCOUNT_ALREADY_EXISTS, name));
            }

            Account.AccountBuilder accountBuilder = new Account.AccountBuilder().builder()
                    .withAccountHolder(name)
                    .withBalance(amount)
                    .withAccountType(type)
                    .withDeleted(false);
            Account account = new Account(accountBuilder);
            account = accountRepository.saveAndFlush(account);
            transactionRepository.save(getTransaction(account, "INITIAL CREDIT", amount));

            return mapToAccountResponse(account);
        } catch (Exception e) {
            throw e;
        }
    }

    private AccountResponse mapToAccountResponse(Account account) {
        return new AccountResponse(account.getId(), account.getAccountHolder(), account.getBalance(), account.getAccountType(), account.isDeleted(), account.getCreatedAt());
    }

    private Transaction getTransaction(Account account, String transactionType, BigDecimal amount) {
        Transaction transaction = new Transaction.TransactionBuilder().builder()
                .withTransactionType(transactionType)
                .withAccount(account)
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
     * @param accountId Checks if account exists for the provided accountId
     * @return Returns the account found
     * @throws AccountNotFoundException Exception is thrown if account not found
     */
    public AccountResponse fetchAccount(final Long accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId).map(this::mapToAccountResponse)
                .orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));
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
     * @param request DTO containing the Source account holder Id, target account holder id and amount to be transferred
     * @throws SelfTransferException Thrown when trying to transfer to self
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal transferFunds(final TransferFundsRequest request) throws SelfTransferException, InsufficientBalanceException, AccountNotFoundException {
        try {
            if (request.accountId().equals(request.targetAccountId())) {
                throw new SelfTransferException(IConstant.SELF_TRANSFER_ERROR);
            }
            Account account = accountRepository.findById(request.accountId()).orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));;
            Account targetAccount = accountRepository.findById(request.targetAccountId()).orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));;

            withdrawMoney(account, request.amount(), "TRANSFER_TO: " + targetAccount.getAccountHolder());
            addMoney(targetAccount, request.amount(), "TRANSFER_FROM: " + account.getAccountHolder());

            return account.getBalance();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Adds money to the account if the amount is positive
     * @param request DTO containing the amount to be added and accountId of the account holder
     * @param transferDesc transfer description
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal addMoney(TransferRequest request, String transferDesc) throws AccountNotFoundException {
        try {
            Account account = accountRepository.findById(request.accountId()).orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));;
            return addMoney(account, request.amount(), transferDesc);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BigDecimal addMoney(Account account, BigDecimal amount, String transferDesc) {
        BigDecimal currentBalance = account.getBalance();
        currentBalance = currentBalance.add(amount);
        account.setBalance(currentBalance);
        transactionRepository.save(getTransaction(account, transferDesc, amount));
        return currentBalance;
    }

    /**
     * Withdraws money from the account if withdrawal possible
     * @param request DTO containing the amount to be withdrawn and accountId of the account holder
     * @param transferDesc transfer description
     * @throws InsufficientBalanceException Thrown when balance is insufficient
     * @throws SQLException Thrown exception when some issue in interacting with db
     */
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal withdrawMoney(TransferRequest request, String transferDesc) throws InsufficientBalanceException, AccountNotFoundException {
        try {
            Account account = accountRepository.findById(request.accountId()).orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));;
            return withdrawMoney(account, request.amount(), transferDesc);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BigDecimal withdrawMoney(Account account, BigDecimal amount, String transferDesc) throws InsufficientBalanceException, AccountNotFoundException {
        try {
            BigDecimal currentBalance = account.getBalance();
            if (!(currentBalance.subtract(amount).compareTo(BigDecimal.ZERO) < 0)) {
                currentBalance = currentBalance.subtract(amount);
                account.setBalance(currentBalance);
                transactionRepository.save(getTransaction(account, transferDesc, amount));
                return currentBalance;
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
    public BigDecimal getAccountBalance(final Long accountId) throws AccountNotFoundException {
        return accountRepository.getBalanceById(accountId).orElseThrow(() -> new AccountNotFoundException(IConstant.ACCOUNT_NOT_FOUND_WITH_ID));
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
