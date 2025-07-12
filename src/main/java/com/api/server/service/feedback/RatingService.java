package com.api.server.service.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.feedback.SaveRatingDTO;
import com.api.server.dto.feedback.ShowRatingDTO;

public interface RatingService {
    ShowRatingDTO save(SaveRatingDTO rating);
    Page<ShowRatingDTO> findAll(Pageable pageable);
    ShowRatingDTO findById(Long id);
    Page<ShowRatingDTO> findByOrderId(Long orderId, Pageable pageable);
} 