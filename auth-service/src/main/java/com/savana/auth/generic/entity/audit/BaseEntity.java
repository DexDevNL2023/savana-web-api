package com.savana.auth.generic.entity.audit;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.savana.auth.generic.entity.GenericEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;


@Document
@Setter
@Getter
public abstract class  BaseEntity<E, D> implements GenericEntity<E, D>, Serializable {
    @Serial
    private static final long serialVersionUID = -8551160985498051566L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdBy;
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;
    @LastModifiedBy
    @Column(name = "updated_by")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedBy;
    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant updatedAt;
    @Column(name = "deleted_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant deletedAt;
    @Column(name = "deleted_by")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String deletedBy;
    @Column(name = "is_deleted")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted = false;

    @PreUpdate
    @PrePersist
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
