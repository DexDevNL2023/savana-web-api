package com.savana.accounting.journal.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.accounting.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountingJournalResponse extends BaseResponse {
    private String direction;
    private Double amount;
    private Double balanceBefore;
    private Double balanceAfter;
    private Long account;
}
