package com.savana.auth.generic.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class  BaseRequest {
    private Long id;
    private boolean isAdd;
    private boolean isUpdated;
    private boolean isDeleted;
}
