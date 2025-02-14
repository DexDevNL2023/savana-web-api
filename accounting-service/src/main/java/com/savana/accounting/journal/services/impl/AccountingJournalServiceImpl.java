package com.savana.accounting.journal.services.impl;

import com.savana.accounting.generic.service.impl.ServiceGenericImpl;
import com.savana.accounting.journal.dto.reponse.AccountingJournalResponse;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;
import com.savana.accounting.journal.mapper.AccountingJournalMapper;
import com.savana.accounting.journal.repositories.AccountingJournalRepository;
import com.savana.accounting.journal.services.AccountingJournalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class AccountingJournalServiceImpl extends ServiceGenericImpl<AccountingJournalRequest, AccountingJournalResponse, AccountingJournal> implements AccountingJournalService {

    private final AccountingJournalRepository repository;
    private final AccountingJournalMapper mapper;

    public AccountingJournalServiceImpl(AccountingJournalRepository repository, AccountingJournalMapper mapper) {
        super(AccountingJournal.class, repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<AccountingJournalResponse> findByAccountId(Long accountId) {
        return repository.findByAccountId(accountId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}