package com.savana.auth.authentification.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotBlank(message = "Veillez renseignez votre login svp!")
    private String email;
    @NotBlank(message = "Veillez renseignez votre mot de passe svp!")
	private String password;
    private Boolean rememberMe = false;
}