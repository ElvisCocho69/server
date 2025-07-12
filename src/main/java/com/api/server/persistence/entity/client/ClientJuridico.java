package com.api.server.persistence.entity.client;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClientJuridico extends Client {

    private String razonsocial;

}