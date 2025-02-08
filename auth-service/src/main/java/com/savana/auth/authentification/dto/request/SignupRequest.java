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
public class SignupRequest {
	@NotBlank(message = "Le nom de l'utilisateur est obligatoire")
	private String name;
	@NotBlank(message = "L'email est obligatoire")
	private String email;
    private String password;

	public static Builder getBuilder() {
		return new Builder();
	}

	public static class Builder {
		private String name;
		private String email;
		private String password;

		public Builder addName(final String name) {
			this.name = name;
			return this;
		}

		public Builder addEmail(final String email) {
			this.email = email;
			return this;
		}

		public Builder addPassword(final String password) {
			this.password = password;
			return this;
		}

		public SignupRequest build() {
            return new SignupRequest(name, email, password);
		}
	}
}