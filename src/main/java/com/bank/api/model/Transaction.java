package com.bank.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 19, scale = 4)
    @NotNull
    private BigDecimal amount;
    @Column
    @NotNull
    private String transactionType;
    @CreationTimestamp
    private LocalDateTime timeStamp;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Transaction(TransactionBuilder builder) {
        this.id = builder.id;
        this.amount = builder.amount;
        this.transactionType = builder.transactionType;
        this.timeStamp = builder.timeStamp;
        this.account = builder.account;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static final class TransactionBuilder {
        private Long id;
        private BigDecimal amount;
        private String transactionType;
        private LocalDateTime timeStamp;
        private Account account;

        public TransactionBuilder() {}

        public TransactionBuilder builder() {return this;}

        public TransactionBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public TransactionBuilder withAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder withTransactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TransactionBuilder withTimestamp(LocalDateTime timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public TransactionBuilder withAccount(Account account) {
            this.account = account;
            return this;
        }

        public Transaction build() {
            return new Transaction(this);
        }
    }
}
