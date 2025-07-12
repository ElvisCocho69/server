package com.api.server.service.order;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.order.OrderDTO;
import com.api.server.dto.order.ShowOrderWithRatingDTO;

public interface OrderService {

    Page<OrderDTO> findAll(Date startDate, Date endDate, String status, Pageable pageable);

    Optional<OrderDTO> findOrderById(Long id);

    Optional<OrderDTO> findOrderByOrdernumber(String ordernumber);

    Page<OrderDTO> findOrderByDateBetweenAndStatus(Date startDate, Date endDate, String status, Pageable pageable);

    OrderDTO saveOrderWithOrderDetailsAndStructure(OrderDTO orderDTO);

    OrderDTO cancelOrder(Long id, String cancellationreason);
    
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);

    // Método para obtener las órdenes de un cliente con sus calificaciones
    Page<ShowOrderWithRatingDTO> findClientOrdersWithRatings(String clientDocumentNumber, Pageable pageable);
}
