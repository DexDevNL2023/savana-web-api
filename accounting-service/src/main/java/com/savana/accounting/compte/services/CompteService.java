package com.savana.accounting.compte.services;

import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.service.ServiceGeneric;
import com.savana.accounting.compte.dto.reponse.CompteResponse;
import com.savana.accounting.compte.dto.request.CompteRequest;

public interface CompteService extends ServiceGeneric<CompteRequest, CompteResponse, Compte> {
    CompteResponse findByUserId(Long userId);
    void creditCompte(String accountId, Double amount);
    void debitCompte(String accountId, Double amount);
}