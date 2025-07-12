package com.api.server.persistence.entity.structure;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Milestones")
@Data
public class Milestone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime date;

    @Column(columnDefinition = "TEXT")
    private String imagepath;

    @Enumerated(EnumType.STRING)
    private MilestoneStage stage;

    public static enum MilestoneStage {
        PENDING,
        MATERIALS_SELECTION,
        CUTTING,
        ASSEMBLING,
        COMPLETED
    }

    @ManyToOne
    @JsonIgnore
    private Structure structure;
}
