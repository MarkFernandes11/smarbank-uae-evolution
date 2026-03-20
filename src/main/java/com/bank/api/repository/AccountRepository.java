package com.bank.api.repository;

import com.bank.api.model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountHolder(String name);

    @Query(value = "SELECT accountHolder FROM Account WHERE deleted = false")
    List<String> getAllAccountHolders();

    @Query(value = "SELECT a.balance FROM accounts a WHERE a.id = ? and a.deleted = false", nativeQuery = true)
    Optional<BigDecimal> getBalanceById(Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE accounts SET balance = :newBalance WHERE id = :accountId", nativeQuery = true)
    int updateBalance(@Param("accountId") Long accountId, @Param("newBalance") BigDecimal newBalance);
}
