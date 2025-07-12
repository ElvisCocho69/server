package com.api.server.dto.security;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowRoles implements Serializable{

    private Long id;

    private String name;

    private List<String> permissions;

}
