package com.api.server.persistence.entity.structure;

import java.util.Date;

import com.api.server.persistence.entity.order.OrderDetail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Structures")
@Getter
@Setter
public class Structure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToOne(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OrderDetail orderdetail;

    private String colors;

    private String materials;

    private Date startdate;

    private Date estimatedenddate;

    private Date realenddate;

    private String observations;

    @OneToOne(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Design design;

}