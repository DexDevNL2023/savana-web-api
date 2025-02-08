package com.savana.accounting.journal.mapper;

import com.savana.accounting.account.services.AccountService;
import com.savana.accounting.generic.mapper.GenericMapper;
import com.savana.accounting.journal.dto.reponse.AccountingJournalResponse;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import com.savana.accounting.journal.entities.AccountingJournal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccountService.class})
public interface AccountingJournalMapper extends GenericMapper<AccountingJournalRequest, AccountingJournalResponse, AccountingJournal> {
}
