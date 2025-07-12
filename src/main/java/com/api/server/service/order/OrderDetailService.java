package com.api.server.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.order.OrderDetailDTO;

public interface OrderDetailService {

    Page<OrderDetailDTO> findAll(Long orderId, Pageable pageable);

    OrderDetailDTO findOrderDetailById(Long id);

    OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);

}
