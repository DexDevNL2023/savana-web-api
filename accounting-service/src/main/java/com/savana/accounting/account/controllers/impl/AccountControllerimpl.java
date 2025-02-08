package com.savana.accounting.account.controllers.impl;

import com.savana.accounting.generic.controller.impl.ControllerGenericImpl;
import com.savana.accounting.account.controllers.AccountController;
import com.savana.accounting.account.dto.reponse.AccountResponse;
import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;
import com.savana.accounting.account.services.AccountService;
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
@RequestMapping("/api/comptes")
@Tag(name = "Comptes", description = "API de gestion des comptes")
public class AccountControllerimpl extends ControllerGenericImpl<AccountRequest, AccountResponse, Account> implements AccountController {

	private static final String MODULE_NAME = "ACCOUNT_MODULE";

	private final AccountService service;

    protected AccountControllerimpl(AccountService service) {
        super(service);
		this.service = service;
    }

    @Override
    protected Account newInstance() {
        return new Account();
    }

    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AccountResponse>> getAccountByUserId(@PathVariable Long userId) {
        AccountResponse response = service.findByUserId(userId);
        return ResponseEntity.ok(new RessourceResponse<>("Compte récupéré avec succès!", response));
    }

    @PutMapping(value = "/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<String>> creditAccount(@RequestParam String accountId, @RequestParam Double amount) {
        service.creditAccount(accountId, amount);
        return ResponseEntity.ok(new RessourceResponse<>("Compte crédité avec succès!", "OK"));
    }

    @PutMapping(value = "/debit", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<String>> debitAccount(@RequestParam String accountId, @RequestParam Double amount) {
        service.debitAccount(accountId, amount);
        return ResponseEntity.ok(new RessourceResponse<>("Compte débité avec succès!", "OK"));
    }
}