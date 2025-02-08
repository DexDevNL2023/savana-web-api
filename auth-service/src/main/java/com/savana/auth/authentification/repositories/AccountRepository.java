package com.savana.auth.authentification.repositories;

import com.savana.auth.authentification.dto.request.AccountRequest;
import com.savana.auth.authentification.entities.Account;
import com.savana.auth.generic.repository.GenericRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends GenericRepository<AccountRequest, Account> {

    @Query("SELECT DISTINCT u FROM Account u WHERE u.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);

    @Query("SELECT DISTINCT u FROM Account u WHERE u.resetPasswordToken = :token")
    Optional<Account> findByResetPasswordToken(@Param("token") String token);

    @Query("SELECT CASE WHEN COUNT(u.id) > 0 THEN TRUE ELSE FALSE END FROM Account u WHERE u.email = :email")
    Boolean existsByEmail(@Param("email") String email);
}
