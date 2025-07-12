package com.api.server.persistence.repository.feedback;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.api.server.persistence.entity.feedback.Rating;
import com.api.server.persistence.entity.order.Order;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Page<Rating> findByOrder(Order order, Pageable pageable);
    boolean existsByOrder(Order order);
    Optional<Rating> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
} 