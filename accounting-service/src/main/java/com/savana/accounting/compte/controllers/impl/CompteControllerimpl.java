package com.savana.accounting.compte.controllers.impl;

import com.savana.accounting.generic.controller.impl.ControllerGenericImpl;
import com.savana.accounting.compte.controllers.CompteController;
import com.savana.accounting.compte.dto.reponse.CompteResponse;
import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.compte.services.CompteService;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounting/comptes")
@Tag(name = "Comptes", description = "API de gestion des comptes")
public class CompteControllerimpl extends ControllerGenericImpl<CompteRequest, CompteResponse, Compte> implements CompteController {

	private static final String MODULE_NAME = "ACCOUNT_MODULE";

	private final CompteService service;

    protected CompteControllerimpl(CompteService service) {
        super(service);
		this.service = service;
    }

    @Override
    protected Compte newInstance() {
        return new Compte();
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<CompteResponse>> getCompteByUserId(@PathVariable Long userId) {
        CompteResponse response = service.findByUserId(userId);
        return ResponseEntity.ok(new RessourceResponse<>("Compte récupéré avec succès!", response));
    }

    @PutMapping(value = "/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<String>> creditCompte(@RequestParam String accountId, @RequestParam Double amount) {
        service.creditCompte(accountId, amount);
        return ResponseEntity.ok(new RessourceResponse<>("Compte crédité avec succès!", "OK"));
    }

    @PutMapping(value = "/debit", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<String>> debitCompte(@RequestParam String accountId, @RequestParam Double amount) {
        service.debitCompte(accountId, amount);
        return ResponseEntity.ok(new RessourceResponse<>("Compte débité avec succès!", "OK"));
    }
}