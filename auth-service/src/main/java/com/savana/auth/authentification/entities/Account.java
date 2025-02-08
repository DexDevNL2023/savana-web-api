package com.savana.auth.authentification.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savana.auth.authentification.dto.request.AccountRequest;
import com.savana.auth.authentification.security.SecurityUtils;
import com.savana.auth.generic.entity.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Account extends BaseEntity<Account, AccountRequest> implements UserDetails {

    private static final String ENTITY_NAME = "UTILISATEUR";
    private static final String MODULE_NAME = "ACCOUNT_MODULE";

    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String resetPasswordToken;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(columnDefinition = "TEXT")
    private String accesToken;

    //userDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityUtils.buildUserAuthorities("User");
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void update(Account source) {
        this.name = source.getName();
        this.email = source.getEmail();
    }

    @Override
    public boolean equalsToDto(AccountRequest source) {
        if (source == null) {
            return false;
        }

        // Comparaison des champs simples avec vérification des valeurs nulles
        boolean areFieldsEqual = (name != null && name.equals(source.getName())) &&
                (email != null && email.equals(source.getEmail()));

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
                ", nomComplet='" + name + '\'' +
                ", email='" + email + '\'' +
        '}';
    }
}
