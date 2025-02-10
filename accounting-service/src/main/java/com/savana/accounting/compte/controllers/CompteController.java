package com.savana.accounting.compte.controllers;

import com.savana.accounting.generic.controller.ControllerGeneric;
import com.savana.accounting.compte.dto.reponse.CompteResponse;
import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface CompteController extends ControllerGeneric<CompteRequest, CompteResponse, Compte> {

    ResponseEntity<RessourceResponse<CompteResponse>> getCompteByUserId(@PathVariable Long userId);
    ResponseEntity<RessourceResponse<String>> creditCompte(@RequestParam String accountId, @RequestParam Double amount);
    ResponseEntity<RessourceResponse<String>> debitCompte(@RequestParam String accountId, @RequestParam Double amount);
}