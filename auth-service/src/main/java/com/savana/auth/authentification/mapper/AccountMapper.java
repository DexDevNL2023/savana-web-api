package com.savana.auth.authentification.mapper;

import com.savana.auth.authentification.dto.reponse.AccountResponse;
import com.savana.auth.authentification.dto.request.AccountRequest;
import com.savana.auth.authentification.dto.request.SignupRequest;
import com.savana.auth.authentification.entities.Account;
import com.savana.auth.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper extends GenericMapper<AccountRequest, AccountResponse, Account> {
    Account toEntity(SignupRequest dto);
}
