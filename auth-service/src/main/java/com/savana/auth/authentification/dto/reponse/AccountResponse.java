package com.savana.auth.authentification.dto.reponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.auth.generic.dto.reponse.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse extends BaseResponse {
    private String name;
    private String email;
}
