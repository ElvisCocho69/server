package com.api.server.dto.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StructureDTO {

    private Long id;
    
    private String name;

    private String description;

    private String colors;

    private String materials;

    private Date startdate;

    private Date estimatedenddate;

    private Date realenddate;

    private String observations;

    private Long orderdetailId;
}