package com.api.server.dto.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveRatingDTO {
    
    @NotNull(message = "El ID de la orden es requerido")
    private Long orderId;

    @NotNull(message = "La calificación es requerida")
    @Min(value = 0, message = "La calificación debe ser entre 0 y 5")
    @Max(value = 5, message = "La calificación debe ser entre 0 y 5")
    private Integer rating;

    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comment;
} 