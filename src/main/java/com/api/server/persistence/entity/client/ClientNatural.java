package com.api.server.persistence.entity.client;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClientNatural extends Client {

    private String name;

    private String lastname;

    private Date birthdate;

}