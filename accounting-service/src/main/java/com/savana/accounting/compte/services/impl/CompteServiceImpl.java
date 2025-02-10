package com.savana.accounting.compte.services.impl;

import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.exceptions.RessourceNotFoundException;
import com.savana.accounting.generic.service.impl.ServiceGenericImpl;
import com.savana.accounting.compte.dto.reponse.CompteResponse;
import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.compte.mapper.CompteMapper;
import com.savana.accounting.compte.repositories.CompteRepository;
import com.savana.accounting.compte.services.CompteService;
import com.savana.accounting.journal.entities.AccountingJournal;
import com.savana.accounting.journal.repositories.AccountingJournalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class CompteServiceImpl extends ServiceGenericImpl<CompteRequest, CompteResponse, Compte> implements CompteService {

    private final CompteRepository repository;
    private final AccountingJournalRepository journalRepository;
    private final CompteMapper mapper;

    public CompteServiceImpl(CompteRepository repository, AccountingJournalRepository journalRepository, CompteMapper mapper) {
        super(Compte.class, repository, mapper);
        this.repository = repository;
        this.journalRepository = journalRepository;
        this.mapper = mapper;
    }

    @Override
    public CompteResponse findByUserId(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toDto)
                .orElseThrow(() -> new RessourceNotFoundException("La ressource compte avec l'id " + userId + " n'existe pas"));
    }

    @Override
    public void creditCompte(String accountId, Double amount) {
        Compte compte = repository.findById(Long.valueOf(accountId))
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        Double newBalance = compte.getBalance() + amount;
        compte.setBalance(newBalance);
        repository.save(compte);

        AccountingJournal journal = new AccountingJournal();
        journal.setCompte(compte);
        journal.setDirection("CREDIT");
        journal.setAmount(amount);
        journal.setBalanceBefore(compte.getBalance() - amount);
        journal.setBalanceAfter(newBalance);
        journalRepository.save(journal);

        log.info("Compte {} credited with {} {}", accountId, amount, compte.getCurrency());
    }

    @Override
    public void debitCompte(String accountId, Double amount) {
        Compte compte = repository.findById(Long.valueOf(accountId))
                .orElseThrow(() -> new RuntimeException("Compte not found"));

        if (compte.getBalance() < amount) {
            throw new RuntimeException("Insufficient funds in compte " + accountId);
        }

        Double newBalance = compte.getBalance() - amount;
        compte.setBalance(newBalance);
        repository.save(compte);

        AccountingJournal journal = new AccountingJournal();
        journal.setCompte(compte);
        journal.setDirection("DEBIT");
        journal.setAmount(amount);
        journal.setBalanceBefore(compte.getBalance() + amount);
        journal.setBalanceAfter(newBalance);
        journalRepository.save(journal);

        log.info("Compte {} debited with {} {}", accountId, amount, compte.getCurrency());
    }
}
