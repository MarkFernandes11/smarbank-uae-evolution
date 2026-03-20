package com.bank.api.repository;

import com.bank.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>  {

    @Query("SELECT t FROM Transaction t JOIN FETCH t.account WHERE t.account.id = :accountId")
    List<Transaction> findAllTransactionByAccountId(@Param("accountId") Long accountId);
}
