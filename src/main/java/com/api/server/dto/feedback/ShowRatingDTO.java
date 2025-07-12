package com.api.server.dto.feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowRatingDTO {
    private Long id;
    private Long orderId;
    private String ordernumber;
    private String clientName;
    private String clientLastname;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
} 