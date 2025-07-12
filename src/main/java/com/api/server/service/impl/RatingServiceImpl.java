package com.api.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.feedback.SaveRatingDTO;
import com.api.server.dto.feedback.ShowRatingDTO;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.feedback.Rating;
import com.api.server.persistence.entity.order.Order;
import com.api.server.persistence.repository.feedback.RatingRepository;
import com.api.server.persistence.repository.order.OrderRepository;
import com.api.server.service.feedback.RatingService;
import com.api.server.persistence.entity.client.Client;
import com.api.server.persistence.entity.client.ClientNatural;
import com.api.server.persistence.entity.client.ClientJuridico;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public ShowRatingDTO save(SaveRatingDTO ratingDTO) {
        Order order = orderRepository.findById(ratingDTO.getOrderId())
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));

        // Verificar si ya existe una calificación para esta orden
        if (ratingRepository.existsByOrder(order)) {
            throw new IllegalArgumentException("Ya existe una calificación para esta orden");
        }

        Rating rating = new Rating();
        rating.setOrder(order);
        rating.setRating(ratingDTO.getRating());
        rating.setComment(ratingDTO.getComment());

        Rating savedRating = ratingRepository.save(rating);
        return convertToDTO(savedRating);
    }

    @Override
    public Page<ShowRatingDTO> findAll(Pageable pageable) {
        return ratingRepository.findAll(pageable)
            .map(this::convertToDTO);
    }

    @Override
    public ShowRatingDTO findById(Long id) {
        return ratingRepository.findById(id)
            .map(this::convertToDTO)
            .orElseThrow(() -> new ObjectNotFoundException("Calificación no encontrada"));
    }

    @Override
    public Page<ShowRatingDTO> findByOrderId(Long orderId, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));
        return ratingRepository.findByOrder(order, pageable)
            .map(this::convertToDTO);
    }

    private ShowRatingDTO convertToDTO(Rating rating) {
        ShowRatingDTO dto = new ShowRatingDTO();
        dto.setId(rating.getId());
        dto.setOrderId(rating.getOrder().getId());
        dto.setOrdernumber(rating.getOrder().getOrdernumber());
        dto.setRating(rating.getRating());
        dto.setComment(rating.getComment());
        dto.setCreatedAt(rating.getCreatedAt());
        
        // Obtener información del cliente
        if (rating.getOrder().getClient() != null) {
            Client client = rating.getOrder().getClient();
            if (client instanceof ClientNatural) {
                ClientNatural naturalClient = (ClientNatural) client;
                dto.setClientName(naturalClient.getName());
                dto.setClientLastname(naturalClient.getLastname());
            } else if (client instanceof ClientJuridico) {
                ClientJuridico juridicClient = (ClientJuridico) client;
                dto.setClientName(juridicClient.getRazonsocial());
                dto.setClientLastname(""); // Para clientes jurídicos, dejamos el apellido vacío
            }
        }
        
        return dto;
    }
} 