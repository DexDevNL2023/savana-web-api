package com.savana.auth.generic.entity.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.savana.auth.generic.entity.GenericEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Document
@JsonIgnoreProperties(
        value = {"createdBy", "createdAt", "updatedBy", "updatedAt", "deletedAt", "deletedBy", "isDeleted"},
        allowGetters = true
)
@Setter
@Getter
public abstract class BaseEntity<E, D> implements GenericEntity<E, D>, Serializable {
    @Serial
    private static final long serialVersionUID = -8551160985498051566L;

    @Id
    private Long id;  // MongoDB utilise un ObjectId sous forme de String

    @CreatedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;

    @CreatedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @LastModifiedBy
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;

    @LastModifiedDate
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant deletedAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

    @PrePersist
    @PreUpdate
    public void beforeAnyUpdate() {
        if (isDeleted) {
            if (deletedBy == null) {
                deletedBy = updatedBy;
            }

            if (deletedAt == null) {
                deletedAt = updatedAt;
            }
        }
    }
}
