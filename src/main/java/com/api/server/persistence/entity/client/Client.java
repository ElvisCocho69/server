package com.api.server.persistence.entity.client;

import java.util.List;

import com.api.server.persistence.entity.order.Order;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.DiscriminatorType;

@Entity
@Table(name = "Clients")
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "client_type", discriminatorType = DiscriminatorType.STRING)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String contact;

    private String address;

    @Enumerated(EnumType.STRING)
    private ClientType clienttype;

    public static enum ClientType {
        NATURAL,
        JURIDICO
    }

    private String documentnumber;

    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    public static enum ClientStatus {
        ENABLED,
        DISABLED
    }

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Order> orders;
}
