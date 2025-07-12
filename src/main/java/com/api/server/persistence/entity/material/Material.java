package com.api.server.persistence.entity.material;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Materials")
@Data
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private MeasurementUnit measurementunit;

    public static enum MeasurementUnit {
        UNIT,
        KILOGRAM,
        GRAM,
        MILLIGRAM,
        METRE,
        SQUARE_METRE,
        CUBIC_METRE,
        CENTIMETRE,
        SQUARE_CENTIMETRE,
        CUBIC_CENTIMETRE,
        MILLIMETRE,
        SQUARE_MILLIMETRE,
        CUBIC_MILLIMETRE,
        LITER,
        MILILITER
    }

    @ManyToOne
    private MaterialCategory materialcategory;

    @ManyToOne
    private Supplier supplier;

    public static enum MaterialStatus {
        ACTIVE,
        INACTIVE
    }

    @Enumerated(EnumType.STRING)
    private MaterialStatus status;

}
