package com.api.server.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.order.ShowOrderWithRatingDTO;
import com.api.server.service.order.OrderService;

@RestController
@RequestMapping("/client/orders")
public class ClientOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<ShowOrderWithRatingDTO>> getClientOrders(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String clientDocumentNumber = authentication.getName();
        
        Page<ShowOrderWithRatingDTO> orders = orderService.findClientOrdersWithRatings(clientDocumentNumber, pageable);
        return ResponseEntity.ok(orders);
    }
} 