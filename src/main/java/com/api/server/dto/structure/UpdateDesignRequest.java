package com.api.server.dto.structure;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDesignRequest {
    
    private String name;

    private String description;

    private String imagepath;

    @NotBlank(message = "La versión es requerida")
    private String version;

}
