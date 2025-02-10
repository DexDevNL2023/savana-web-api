package com.savana.accounting.compte.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.accounting.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompteRequest extends BaseRequest {

    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String accountNumber;

    @NotNull(message = "Le solde initial est obligatoire")
    @PositiveOrZero(message = "Le solde initial ne peut pas être négatif")
    private Double balance;

    @NotBlank(message = "La devise est obligatoire")
    private String currency;

    private List<Long> journaux = new ArrayList<>();

    @NotNull(message = "L'identifiant utilisateur est obligatoire")
    private Long userId;
}
