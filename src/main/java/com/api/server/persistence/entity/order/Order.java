package com.api.server.persistence.entity.order;

import java.util.Date;
import java.util.List;

import com.api.server.persistence.entity.client.Client;
import com.api.server.persistence.entity.security.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ordernumber;

    private Date orderdate;

    private Date deliverydate;

    private String description;

    private String specialnotes;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public static enum OrderStatus {
        PENDIENTE,
        EN_PREPARACION,
        ENTREGADO,
        CANCELADO
    }

    private String cancellationreason;

    private Double totalprice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderDetail> orderdetails;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
