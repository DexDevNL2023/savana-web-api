package com.savana.accounting.journal.controllers;

import com.savana.accounting.generic.controller.ControllerGeneric;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import com.savana.accounting.journal.dto.reponse.AccountingJournalResponse;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountingJournalController extends ControllerGeneric<AccountingJournalRequest, AccountingJournalResponse, AccountingJournal> {
    ResponseEntity<RessourceResponse<List<AccountingJournalResponse>>> getJournalsByAccountId(@PathVariable Long accountId);
}