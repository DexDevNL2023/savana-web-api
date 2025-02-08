package com.savana.accounting.account.mapper;

import com.savana.accounting.account.dto.reponse.AccountResponse;
import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;
import com.savana.accounting.generic.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper extends GenericMapper<AccountRequest, AccountResponse, Account> {

    @Mapping(target = "journaux", ignore = true)
    Account toEntity(AccountRequest dto);

    @Mapping(target = "journaux", ignore = true)
    AccountResponse toDto(Account entity);
}
