package com.api.server.persistence.entity.material;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Material_Movements")
@Data
public class MaterialMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Material material;

    @Enumerated(EnumType.STRING)
    private MovementType movementtype;

    public static enum MovementType {
        IN,
        OUT,
        ADJUSTMENT,
        LOSS,
        RETURN,
    }

    private Double quantity;

    private LocalDateTime movementdate;

    private String description;

    private Long userid;

}
