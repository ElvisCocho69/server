package com.api.server.dto.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DesignResponse {

    private Long id;

    private String name;

    private String description;

    private String imagepath;

    private String version;

    private Date createdAt;

    private Date updatedAt;

    private StructureDTO structure;
}
