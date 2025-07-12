package com.api.server.dto.order;

import java.util.Date;
import java.util.List;

import com.api.server.persistence.entity.order.Order.OrderStatus;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {

    @Column(unique = true)
    private String ordernumber;

    private Date orderdate;

    private Date deliverydate;

    private String description;

    private String specialnotes;

    private OrderStatus status;

    private String cancellationreason;

    private Double totalprice;

    private Long clientId;

    private Long userId;

    private List<OrderDetailDTO> orderDetails;

}
