package com.savana.accounting.account.controllers;

import com.savana.accounting.generic.controller.ControllerGeneric;
import com.savana.accounting.account.dto.reponse.AccountResponse;
import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface AccountController extends ControllerGeneric<AccountRequest, AccountResponse, Account> {

    ResponseEntity<RessourceResponse<AccountResponse>> getAccountByUserId(@PathVariable Long userId);
    ResponseEntity<RessourceResponse<String>> creditAccount(@RequestParam String accountId, @RequestParam Double amount);
    ResponseEntity<RessourceResponse<String>> debitAccount(@RequestParam String accountId, @RequestParam Double amount);
}