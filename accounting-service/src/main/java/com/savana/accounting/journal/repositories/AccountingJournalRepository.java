package com.savana.accounting.journal.repositories;

import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;
import com.savana.accounting.generic.repository.GenericRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountingJournalRepository extends GenericRepository<AccountingJournalRequest, AccountingJournal> {

    @Query("SELECT j FROM AccountingJournal j WHERE j.compte.id = :accountId")
    List<AccountingJournal> findByAccountId(@Param("accountId") Long accountId);
}
