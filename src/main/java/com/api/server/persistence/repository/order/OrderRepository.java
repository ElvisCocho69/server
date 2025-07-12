package com.api.server.persistence.repository.order;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrdernumber(String ordernumber);

    Page<Order> findByOrderdateBetween(Date startDate, Date endDate, Pageable pageable);

    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);

    Page<Order> findByOrderdateBetweenAndStatus(Date startDate, Date endDate, Order.OrderStatus status, Pageable pageable);

    Page<Order> findByClientId(Long clientId, Pageable pageable);

}
