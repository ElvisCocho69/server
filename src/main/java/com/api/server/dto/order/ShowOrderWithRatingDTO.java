package com.api.server.dto.order;

import com.api.server.dto.feedback.ShowRatingDTO;
import com.api.server.persistence.entity.order.Order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowOrderWithRatingDTO {
    private Long id;
    private String ordernumber;
    private LocalDateTime createdAt;
    private LocalDateTime deliveryDate;
    private OrderStatus status;
    private ShowRatingDTO rating;
} 