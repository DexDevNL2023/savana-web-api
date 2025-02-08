package com.savana.accounting.account.services.impl;

import com.savana.accounting.generic.exceptions.RessourceNotFoundException;
import com.savana.accounting.generic.service.impl.ServiceGenericImpl;
import com.savana.accounting.account.dto.reponse.AccountResponse;
import com.savana.accounting.account.dto.request.AccountRequest;
import com.savana.accounting.account.entities.Account;
import com.savana.accounting.account.mapper.AccountMapper;
import com.savana.accounting.account.repositories.AccountRepository;
import com.savana.accounting.account.services.AccountService;
import com.savana.accounting.journal.entities.AccountingJournal;
import com.savana.accounting.journal.repositories.AccountingJournalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class AccountServiceImpl extends ServiceGenericImpl<AccountRequest, AccountResponse, Account> implements AccountService {

    private final AccountRepository repository;
    private final AccountingJournalRepository journalRepository;
    private final AccountMapper mapper;

    public AccountServiceImpl(AccountRepository repository, AccountingJournalRepository journalRepository, AccountMapper mapper) {
        super(Account.class, repository, mapper);
        this.repository = repository;
        this.journalRepository = journalRepository;
        this.mapper = mapper;
    }

    @Override
    public AccountResponse findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new RessourceNotFoundException("La ressource account avec l'id " + userId + " n'existe pas"));
    }

    @Override
    public void creditAccount(String accountId, Double amount) {
        Account account = repository.findById(Long.valueOf(accountId))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        Double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        repository.save(account);

        AccountingJournal journal = new AccountingJournal();
        journal.setAccount(account);
        journal.setDirection("CREDIT");
        journal.setAmount(amount);
        journal.setBalanceBefore(account.getBalance() - amount);
        journal.setBalanceAfter(newBalance);
        journalRepository.save(journal);

        log.info("Account {} credited with {} {}", accountId, amount, account.getCurrency());
    }

    @Override
    public void debitAccount(String accountId, Double amount) {
        Account account = repository.findById(Long.valueOf(accountId))
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds in account " + accountId);
        }

        Double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        repository.save(account);

        AccountingJournal journal = new AccountingJournal();
        journal.setAccount(account);
        journal.setDirection("DEBIT");
        journal.setAmount(amount);
        journal.setBalanceBefore(account.getBalance() + amount);
        journal.setBalanceAfter(newBalance);
        journalRepository.save(journal);

        log.info("Account {} debited with {} {}", accountId, amount, account.getCurrency());
    }
}
