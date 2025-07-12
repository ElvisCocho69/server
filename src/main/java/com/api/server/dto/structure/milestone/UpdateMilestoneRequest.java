package com.api.server.dto.structure.milestone;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMilestoneRequest {

    private String description;

    private LocalDateTime date;

    private Long structureId;
}
