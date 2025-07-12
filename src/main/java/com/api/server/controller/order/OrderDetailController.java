package com.api.server.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.order.OrderDetailDTO;
import com.api.server.service.order.OrderDetailService;

@RestController
@RequestMapping("/order-details")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public ResponseEntity<Page<OrderDetailDTO>> listAllOrderDetails(
        @RequestParam(required = false) Long orderId,
        Pageable pageable
    ) {
        try {
            Page<OrderDetailDTO> orderDetails = orderDetailService.findAll(orderId, pageable);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> findOrderDetailById(@PathVariable Long id) {
        OrderDetailDTO orderDetail = orderDetailService.findOrderDetailById(id);
        return ResponseEntity.ok(orderDetail);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<Page<OrderDetailDTO>> listOrderDetailById(
        @PathVariable Long id,
        Pageable pageable
    ) {
        try {
            Page<OrderDetailDTO> orderDetails = orderDetailService.findAll(id, pageable);
            return ResponseEntity.ok(orderDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDetailDTO> updateOrderDetail(
        @PathVariable Long id,
        @RequestBody OrderDetailDTO orderDetailDTO
    ) {
        try {
            OrderDetailDTO updatedOrderDetail = orderDetailService.updateOrderDetail(id, orderDetailDTO);
            return ResponseEntity.ok(updatedOrderDetail);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
