package com.savana.accounting.account.services;

import com.savana.accounting.generic.service.ServiceGeneric;
import com.savana.accounting.account.dto.reponse.AccountResponse;
import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;

public interface AccountService extends ServiceGeneric<AccountRequest, AccountResponse, Account> {
    AccountResponse findByUserId(Long userId);
    void creditAccount(String accountId, Double amount);
    void debitAccount(String accountId, Double amount);
}