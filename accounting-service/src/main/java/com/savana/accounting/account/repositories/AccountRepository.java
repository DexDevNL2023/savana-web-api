package com.savana.accounting.account.repositories;

import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;
import com.savana.accounting.generic.repository.GenericRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends GenericRepository<AccountRequest, Account> {

    @Query("SELECT a FROM Account a WHERE a.userId = :userId")
    Optional<Account> findByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Account a WHERE a.userId = :userId")
    Boolean existsByUserId(@Param("userId") Long userId);
}
