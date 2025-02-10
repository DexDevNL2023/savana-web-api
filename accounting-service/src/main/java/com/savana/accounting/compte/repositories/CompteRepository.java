package com.savana.accounting.compte.repositories;

import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.repository.GenericRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompteRepository extends GenericRepository<CompteRequest, Compte> {

    @Query("SELECT a FROM Compte a WHERE a.userId = :userId")
    Optional<Compte> findByUserId(@Param("userId") Long userId);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Compte a WHERE a.userId = :userId")
    Boolean existsByUserId(@Param("userId") Long userId);
}
