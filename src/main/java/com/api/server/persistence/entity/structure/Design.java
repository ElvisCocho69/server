package com.api.server.persistence.entity.structure;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Designs")
@Getter
@Setter
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String imagepath;

    private String version;

    private Date createdAt;

    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "structure_id")
    private Structure structure;
}
