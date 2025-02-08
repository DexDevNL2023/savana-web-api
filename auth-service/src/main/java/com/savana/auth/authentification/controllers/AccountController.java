package com.savana.auth.authentification.controllers;

import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.request.AccountRequest;
import com.savana.auth.authentification.dto.request.ChangePasswordRequest;
import com.savana.auth.authentification.entities.Account;
import com.savana.auth.generic.controller.ControllerGeneric;
import com.savana.auth.generic.dto.reponse.RessourceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AccountController extends ControllerGeneric<AccountRequest, AccountResponse, Account> {
	ResponseEntity<RessourceResponse<AccountResponse>> getCurrentUser();
	ResponseEntity<RessourceResponse<Boolean>> logout();
	ResponseEntity<RessourceResponse<AccountResponse>> changePassword(@Valid @RequestBody ChangePasswordRequest dto);
}