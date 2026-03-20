package com.bank.api.model;

import com.bank.api.enums.AccountType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@SQLDelete(sql = "UPDATE accounts SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false") // This ensures deleted accounts don't show up in normal queries
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NotNull
    private String accountHolder;
    @Column(precision = 19, scale = 4)
    @NotNull
    private BigDecimal balance;

    @Column(nullable = false)
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    public Account () {}

    /**
     * Constructor to initialize an account based on the accountBuilder
     *
     * @param accountBuilder Account Builder to populate the details of the account holder
     */
    public Account(AccountBuilder accountBuilder) {
        this.id = accountBuilder.id;
        this.balance = accountBuilder.balance;
        this.accountHolder = accountBuilder.accountHolder;
        this.deleted = accountBuilder.deleted;
        this.accountType = accountBuilder.accountType;
        this.createdAt = accountBuilder.createdAt;
        this.updatedAt = accountBuilder.updatedAt;
        this.transactions = accountBuilder.transactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static final class AccountBuilder {
        private Long id;
        private String accountHolder;
        private BigDecimal balance;
        private boolean deleted;
        private AccountType accountType;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private List<Transaction> transactions;

        public AccountBuilder() {}

        public AccountBuilder builder() {
            return this;
        }

        public AccountBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public AccountBuilder withAccountHolder(String accountHolder) {
            this.accountHolder = accountHolder;
            return this;
        }

        public AccountBuilder withBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountBuilder withDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public AccountBuilder withAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        public AccountBuilder withCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public AccountBuilder withUpdatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AccountBuilder withTransactions(List<Transaction> transactions) {
            this.transactions = transactions;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

    }

}
