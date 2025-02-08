package com.savana.auth.authentification.controllers;

import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.reponse.JwtAuthenticationResponse;
import com.savana.auth.authentification.dto.request.LoginRequest;
import com.savana.auth.authentification.dto.request.ResetPasswordRequest;
import com.savana.auth.authentification.dto.request.SignupRequest;
import com.savana.auth.authentification.services.AccountService;
import com.savana.auth.generic.dto.reponse.RessourceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/authentifications")
@Tag(name = "Authentifications", description = "API de gestion des authentifications")
public class AuthentificationController {

    private final AccountService accountService;

    public AuthentificationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<JwtAuthenticationResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse jwtDto = accountService.login(loginRequest);
        return new ResponseEntity<>(new RessourceResponse<JwtAuthenticationResponse>("L'utilisateur s'est connecté avec succès!", jwtDto), HttpStatus.OK);
    }
    
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<AccountResponse>> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return new ResponseEntity<>(new RessourceResponse<AccountResponse>("Utilisateur enregistré avec succès!", accountService.register(signUpRequest)), HttpStatus.OK);
    }

    @PostMapping(value = "/password/forgot/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<Boolean>> forgotPassword(@NotNull @PathVariable(value = "email") String email) {
        return new ResponseEntity<>(new RessourceResponse<Boolean>("Jeton de mot de passe oublié avec succès!", accountService.forgotPassword(email)), HttpStatus.OK);
    }

    @PostMapping(value = "/password/reset", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RessourceResponse<Boolean>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return new ResponseEntity<>(new RessourceResponse<Boolean>("Réinitialiser le mot de passe avec succès!", accountService.resetPassword(resetPasswordRequest)), HttpStatus.OK);
    }
}