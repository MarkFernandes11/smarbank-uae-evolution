package com.bank.api.repository;

import com.bank.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountHolder(String name);

    List<String> getAllAccountHolder();

    BigDecimal getBalanceById(Long id);

    @Query(value = "UPDATE accounts SET balance = :newBalance WHERE id = :accountId")
    int updateBalance(Long accountId, BigDecimal newBalance);
}
