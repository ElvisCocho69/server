package com.api.server.controller.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.feedback.SaveRatingDTO;
import com.api.server.dto.feedback.ShowRatingDTO;
import com.api.server.exception.UnauthorizedException;
import com.api.server.persistence.entity.order.Order;
import com.api.server.persistence.repository.order.OrderRepository;
import com.api.server.service.feedback.RatingService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<ShowRatingDTO> create(@RequestBody @Valid SaveRatingDTO rating) {
        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Verificar que la orden pertenece al cliente
        Order order = orderRepository.findById(rating.getOrderId())
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (!order.getClient().getDocumentnumber().equals(username)) {
            throw new UnauthorizedException("No tienes permiso para calificar esta orden");
        }

        return ResponseEntity.ok(ratingService.save(rating));
    }

    @GetMapping
    public ResponseEntity<Page<ShowRatingDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ratingService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowRatingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.findById(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<ShowRatingDTO>> findByOrderId(@PathVariable Long orderId, Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada"));

        if (!order.getClient().getDocumentnumber().equals(username)) {
            throw new UnauthorizedException("No tienes permiso para ver las calificaciones de esta orden");
        }

        return ResponseEntity.ok(ratingService.findByOrderId(orderId, pageable));
    }
} 