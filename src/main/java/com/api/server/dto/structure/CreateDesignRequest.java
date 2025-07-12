package com.api.server.dto.structure;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDesignRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    private String name;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 255, message = "La descripción debe tener entre 1 y 255 caracteres")
    private String description;

    private String imagepath;

    @NotBlank(message = "La versión es requerida")
    private String version;

    @NotNull(message = "La estructura es requerida")
    private Long structureId;
}
