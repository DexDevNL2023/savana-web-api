package com.savana.accounting.journal.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.accounting.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountingJournalRequest extends BaseRequest {

    @NotBlank(message = "La direction est obligatoire")
    @Pattern(regexp = "CREDIT|DEBIT", message = "La direction doit être 'CREDIT' ou 'DEBIT'")
    private String direction;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à zéro")
    private Double amount;

    @NotNull(message = "Le solde avant est obligatoire")
    @PositiveOrZero(message = "Le solde avant doit être positif ou nul")
    private Double balanceBefore;

    @NotNull(message = "Le solde après est obligatoire")
    @PositiveOrZero(message = "Le solde après doit être positif ou nul")
    private Double balanceAfter;

    @NotNull(message = "Le compte utilisateur est obligatoire")
    private Long account;
}
