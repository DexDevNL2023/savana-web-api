package com.savana.auth.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.savana.auth.generic.dto.request.BaseRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRequest extends BaseRequest {
    @NotBlank(message = "Le nom de l'utilisateur est obligatoire")
    private String name;
	@NotBlank(message = "L'email est obligatoire")
    private String email;
}
