package com.savana.accounting.journal.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savana.accounting.compte.entities.Compte;
import com.savana.accounting.generic.entity.audit.BaseEntity;
import com.savana.accounting.journal.dto.request.AccountingJournalRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounting_journal")
public class AccountingJournal extends BaseEntity<AccountingJournal, AccountingJournalRequest> {

    private static final String ENTITY_NAME = "JOURNAL";
    private static final String MODULE_NAME = "ACCOUNTING_JOURNAL_MODULE";

    @Column(nullable = false, unique = true)
    private String direction;

    private Double amount;

    private Double balanceBefore;
    private Double balanceAfter;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Compte compte;

    @Override
    public void update(AccountingJournal source) {
        this.direction = source.getDirection();
        this.amount = source.getAmount();
        this.balanceBefore = source.getBalanceBefore();
        this.balanceAfter = source.getBalanceAfter();
        this.compte = source.getCompte();
    }

    @Override
    public boolean equalsToDto(AccountingJournalRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples avec vérification des valeurs nulles
        boolean areFieldsEqual = (direction != null && direction.equals(source.getDirection())) &&
                (amount != null && amount.equals(source.getAmount())) &&
                (balanceBefore != null && balanceBefore.equals(source.getBalanceBefore())) &&
                (balanceAfter != null && balanceAfter.equals(source.getBalanceAfter()));

        return areFieldsEqual;
    }

    @Override
    public String getEntityName() {
        return ENTITY_NAME;
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "id=" + getId() +
                ", direction='" + direction + '\'' +
                ", amount='" + amount + '\'' +
                ", balanceBefore='" + balanceBefore + '\'' +
                ", balanceAfter='" + balanceAfter + '\'' +
        '}';
    }
}
