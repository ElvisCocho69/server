package com.api.server.dto.structure.milestone;

import java.time.LocalDateTime;

import com.api.server.persistence.entity.structure.Milestone.MilestoneStage;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMilestoneRequest {

    @NotBlank(message = "La descripción es requerida.")
    private String description;

    @NotBlank(message = "La fecha es requerida.")
    private LocalDateTime date;

    @NotBlank(message = "La etapa es requerida.")
    private MilestoneStage stage;

    @NotBlank(message = "La id de la estructura es requerida.")
    private Long structureId;
    
}
