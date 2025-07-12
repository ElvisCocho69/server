package com.api.server.persistence.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.order.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    Page<OrderDetail> findByOrderId(Long orderId, Pageable pageable);

}
