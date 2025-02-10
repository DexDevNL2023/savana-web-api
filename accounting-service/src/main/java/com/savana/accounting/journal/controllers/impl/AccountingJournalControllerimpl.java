package com.savana.accounting.journal.controllers.impl;

import com.savana.accounting.generic.controller.impl.ControllerGenericImpl;
import com.savana.accounting.generic.dto.reponse.RessourceResponse;
import com.savana.accounting.journal.controllers.AccountingJournalController;
import com.savana.accounting.journal.dto.reponse.AccountingJournalResponse;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;
import com.savana.accounting.journal.services.AccountingJournalService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RefreshScope
@ResponseBody
@RestController
@CrossOrigin("*")
@RequestMapping("/api/accounting/journal")
@Tag(name = "Journaux", description = "API de gestion des journaux des comptes")
public class AccountingJournalControllerimpl extends ControllerGenericImpl<AccountingJournalRequest, AccountingJournalResponse, AccountingJournal> implements AccountingJournalController {

	private static final String MODULE_NAME = "ACCOUNTING_JOURNAL_MODULE";

	private final AccountingJournalService service;

    protected AccountingJournalControllerimpl(AccountingJournalService service) {
        super(service);
		this.service = service;
    }

    @Override
    protected AccountingJournal newInstance() {
        return new AccountingJournal();
    }

    @GetMapping(value = "/compte/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<RessourceResponse<List<AccountingJournalResponse>>> getJournalsByAccountId(@PathVariable Long accountId) {
        List<AccountingJournalResponse> journalResponses = service.findByAccountId(accountId);
        return ResponseEntity.ok(new RessourceResponse<>("Journaux récupérés avec succès!", journalResponses));
    }
}