package com.savana.accounting.account.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.accounting.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse extends BaseResponse {
    private String accountNumber;
    private Double balance;
    private String currency;
    private List<Long> journaux = new ArrayList<>();
    private Long userId;
}
