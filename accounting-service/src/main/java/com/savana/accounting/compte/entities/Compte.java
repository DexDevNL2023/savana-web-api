package com.savana.accounting.compte.entities;

import com.savana.accounting.compte.dto.request.CompteRequest;
import com.savana.accounting.generic.entity.audit.BaseEntity;
import com.savana.accounting.journal.entities.AccountingJournal;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "accounts")
public class Compte extends BaseEntity<Compte, CompteRequest> {

    private static final String ENTITY_NAME = "COMPTE CLIENT";
    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    @Column(nullable = false, unique = true)
    private String accountNumber;

    private Double balance;

    @Column(nullable = false)
    private String currency;

    @OneToMany(mappedBy = "compte", fetch = FetchType.EAGER)
    private List<AccountingJournal> journaux = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private Long userId;

    @Override
    public void update(Compte source) {
        this.accountNumber = source.getAccountNumber();
        this.balance = source.getBalance();
        this.currency = source.getCurrency();
        this.journaux = source.getJournaux();
        this.userId = source.getUserId();
    }

    @Override
    public boolean equalsToDto(CompteRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples avec vérification des valeurs nulles
        boolean areFieldsEqual = (accountNumber != null && accountNumber.equals(source.getAccountNumber())) &&
                (balance != null && balance.equals(source.getBalance())) &&
                (currency != null && currency.equals(source.getCurrency())) &&
                (userId != null && userId.equals(source.getUserId()));

        // Comparaison des journaux
        boolean areJournauxEqual = (source.getJournaux() == null && journaux.isEmpty()) ||
                (source.getJournaux() != null && source.getJournaux().size() == getJournaux().size());

        return areFieldsEqual && areJournauxEqual;
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
                ", accountNumber='" + accountNumber + '\'' +
                ", balance='" + balance + '\'' +
                ", currency='" + currency + '\'' +
                ", journaux=" + journaux.stream()
                .map(journal -> "Journal{id=" + journal.getId() + "'}") +
        '}';
    }
}
