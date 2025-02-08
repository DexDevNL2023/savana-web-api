package com.savana.accounting.journal.services;

import com.savana.accounting.generic.service.ServiceGeneric;
import com.savana.accounting.journal.dto.reponse.AccountingJournalResponse;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;

import java.util.List;

public interface AccountingJournalService extends ServiceGeneric<AccountingJournalRequest, AccountingJournalResponse, AccountingJournal> {

    List<AccountingJournalResponse> findByAccountId(Long accountId);
}