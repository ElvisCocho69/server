package com.api.server.dto.security;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRoleWithPermissions implements Serializable {

    @NotBlank
    private String name;

    @NotEmpty
    private List<Long> operationIds;

}
