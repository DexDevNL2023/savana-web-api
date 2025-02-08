package com.savana.auth.authentification.controllers.impl;

import com.savana.auth.authentification.controllers.AccountController;
import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.request.AccountRequest;
import com.savana.auth.authentification.dto.request.ChangePasswordRequest;
import com.savana.auth.authentification.entities.Account;
import com.savana.auth.authentification.services.AccountService;
import com.savana.auth.generic.controller.impl.ControllerGenericImpl;
import com.savana.auth.generic.dto.reponse.RessourceResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounts")
@Tag(name = "Utilisateurs", description = "API de gestion des utilisateurs")
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

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<AccountResponse>> getCurrentUser() {
		return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Utilisateur trouvé avec succès!", service.getCurrentUser()), HttpStatus.OK);
	}

	@PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
	public ResponseEntity<RessourceResponse<Boolean>> logout() {
		return new ResponseEntity<>(new RessourceResponse<Boolean>("Déconnexion de l'utilisateur réussie!", service.logout()), HttpStatus.OK);
	}

	@PutMapping(value = "/change/password", produces = MediaType.APPLICATION_JSON_VALUE)
	@SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<AccountResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest userFormPasswordRequest) {
		return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Mot de passe utilisateur mis à jour avec succès!", service.changePassword(userFormPasswordRequest)), HttpStatus.OK);
	}
}