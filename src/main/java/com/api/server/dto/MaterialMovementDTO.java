package com.api.server.dto;

import lombok.Data;

@Data
public class MaterialMovementDTO {
    private String movementType;
    private Double quantity;
    private String description;
    private Long userId;
} 