package com.savana.auth.authentification.services;

import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.reponse.JwtAuthenticationResponse;
import com.savana.auth.authentification.dto.request.*;
import com.savana.auth.authentification.entities.Account;
import com.savana.auth.generic.service.ServiceGeneric;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface AccountService extends ServiceGeneric<AccountRequest, AccountResponse, Account>, UserDetailsService {
    AccountResponse register(SignupRequest dto);
    JwtAuthenticationResponse login(LoginRequest dto);
    Boolean logout();
    AccountResponse changePassword(ChangePasswordRequest dto);
    Account loadCurrentUser();
    AccountResponse getCurrentUser();
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    Boolean forgotPassword(String email);
    Boolean resetPassword(ResetPasswordRequest resetPasswordRequest);
    Account saveDefault(Account newAccount);
}