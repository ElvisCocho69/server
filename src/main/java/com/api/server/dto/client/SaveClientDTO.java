package com.api.server.dto.client;

import java.util.Date;

import com.api.server.persistence.entity.client.Client.ClientStatus;
import com.api.server.persistence.entity.client.Client.ClientType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveClientDTO {
    
    @NotNull
    private ClientType clientType;
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String contact;
    
    @NotBlank
    private String address;
    
    @NotBlank
    private String documentnumber;
    
    private ClientStatus status;
    
    // Campos para ClientNatural
    private String name;
    private String lastname;
    private Date birthdate;
    
    // Campos para ClientJuridico
    private String razonsocial;
} 