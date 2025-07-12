package com.api.server.dto.client;

import java.util.Date;

import com.api.server.persistence.entity.client.Client.ClientStatus;
import com.api.server.persistence.entity.client.Client.ClientType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowClientDTO {
    private Long id;
    private String email;
    private String contact;
    private String address;
    private ClientType clientType;
    private ClientStatus status;
    private String documentnumber;
    
    // Campos para ClientNatural
    private String name;
    private String lastname;
    private Date birthdate;

    // Campos para ClientJuridico
    private String razonsocial;
} 