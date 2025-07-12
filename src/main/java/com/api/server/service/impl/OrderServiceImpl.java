package com.api.server.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.order.OrderDTO;
import com.api.server.dto.order.OrderDetailDTO;
import com.api.server.dto.order.ShowOrderWithRatingDTO;
import com.api.server.dto.structure.StructureDTO;
import com.api.server.dto.feedback.ShowRatingDTO;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.order.Order;
import com.api.server.persistence.entity.order.OrderDetail;
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.persistence.entity.client.Client;
import com.api.server.persistence.entity.feedback.Rating;
import com.api.server.persistence.repository.order.OrderRepository;
import com.api.server.persistence.repository.client.ClientRepository;
import com.api.server.persistence.repository.security.UserRepository;
import com.api.server.persistence.repository.structure.StructureRepository;
import com.api.server.persistence.repository.feedback.RatingRepository;
import com.api.server.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public Page<OrderDTO> findAll(Date startDate, Date endDate, String status, Pageable pageable) {
        // 1. Buscar las ordenes
        Page<Order> orders;
        if (startDate != null && endDate != null && status != null) {
            orders = orderRepository.findByOrderdateBetweenAndStatus(startDate, endDate, Order.OrderStatus.valueOf(status), pageable);
        } else if (startDate != null && endDate != null) {
            orders = orderRepository.findByOrderdateBetween(startDate, endDate, pageable);
        } else if (status != null) {
            orders = orderRepository.findByStatus(Order.OrderStatus.valueOf(status), pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        // 2. Devolver el DTO de las ordenes
        return orders.map(this::mapToDTO);
    }

    @Override
    public Optional<OrderDTO> findOrderById(Long id) {
        // 1. Buscar la orden
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));

        // 2. Devolver el DTO de la orden
        return Optional.of(mapToDTO(order));
    }

    @Override
    public Optional<OrderDTO> findOrderByOrdernumber(String ordernumber) {
        // 1. Buscar la orden
        Order order = orderRepository.findByOrdernumber(ordernumber)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));

        // 2. Devolver el DTO de la orden
        return Optional.of(mapToDTO(order));
    }

    @Override
    public Page<OrderDTO> findOrderByDateBetweenAndStatus(Date startDate, Date endDate, String status, Pageable pageable) {
        // 1. Buscar las ordenes
        Page<Order> orders = orderRepository.findByOrderdateBetweenAndStatus(startDate, endDate, Order.OrderStatus.valueOf(status), pageable);

        // 2. Devolver el DTO de las ordenes
        return orders.map(this::mapToDTO);
    }

    @Override
    public OrderDTO saveOrderWithOrderDetailsAndStructure(OrderDTO orderDTO) {
        // 1. Crear y mapear la orden
        Order order = new Order();
        order.setOrdernumber(orderDTO.getOrdernumber());
        order.setOrderdate(orderDTO.getOrderdate());
        order.setDeliverydate(orderDTO.getDeliverydate());
        order.setDescription(orderDTO.getDescription());
        order.setSpecialnotes(orderDTO.getSpecialnotes());
        order.setStatus(orderDTO.getStatus());
        order.setTotalprice(orderDTO.getTotalprice());
        order.setClient(clientRepository.findById(orderDTO.getClientId())
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado")));
        order.setUser(userRepository.findById(orderDTO.getUserId())
            .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado")));

        // 2. Crear y mapear los detalles de orden con sus estructuras
        List<OrderDetail> orderDetails = orderDTO.getOrderDetails().stream()
            .map(detailDTO -> {
                // Crear y guardar estructura primero
                Structure structure = new Structure();
                structure.setName(detailDTO.getStructure().getName());
                structure.setDescription(detailDTO.getStructure().getDescription());
                structure.setColors(detailDTO.getStructure().getColors());
                structure.setMaterials(detailDTO.getStructure().getMaterials());
                structure.setStartdate(detailDTO.getStructure().getStartdate());
                structure.setEstimatedenddate(detailDTO.getStructure().getEstimatedenddate());
                structure.setRealenddate(detailDTO.getStructure().getRealenddate());
                structure.setObservations(detailDTO.getStructure().getObservations());
                
                // Guardar la estructura primero
                structure = structureRepository.save(structure);

                // Crear detalle de orden
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(detailDTO.getQuantity());
                orderDetail.setUnitprice(detailDTO.getUnitprice());
                orderDetail.setCancellationreason(detailDTO.getCancellationreason());
                orderDetail.setStructure(structure);
                orderDetail.setOrder(order);

                return orderDetail;
            })
            .collect(Collectors.toList());

        // 3. Establecer la lista de detalles en la orden
        order.setOrderdetails(orderDetails);

        // 4. Guardar todo en una sola transacción
        Order savedOrder = orderRepository.save(order);

        return mapToDTO(savedOrder);
    }

    @Override
    public OrderDTO cancelOrder(Long id, String cancellationreason) {
        // 1. Validar la razón de cancelación
        if (cancellationreason == null || cancellationreason.trim().isEmpty()) {
            throw new IllegalArgumentException("Se requiere una razón de cancelación");
        }

        // 2. Buscar la orden
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));

        // 3. Validar que la orden no esté ya cancelada
        if (order.getStatus() == Order.OrderStatus.CANCELADO) {
            throw new IllegalStateException("La orden ya está cancelada");
        }

        // 4. Actualizar el estado y la razón de cancelación
        order.setStatus(Order.OrderStatus.CANCELADO);
        order.setCancellationreason(cancellationreason.trim());

        // 5. Guardar la orden
        order = orderRepository.save(order);

        // 6. Devolver el DTO de la orden
        return mapToDTO(order);
    }

    @Override
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        // 1. Validar el DTO
        if (orderDTO == null) {
            throw new IllegalArgumentException("La orden no puede ser nula");
        }

        // 2. Buscar la orden
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada"));

        // 3. Validar que no esté cancelada
        if (order.getStatus() == Order.OrderStatus.CANCELADO) {
            throw new IllegalStateException("No se puede modificar una orden cancelada");
        }

        // 4. Validar y actualizar los datos de la orden
        if (orderDTO.getStatus() != null) {
            // Validar transición de estado
            if (orderDTO.getStatus() == Order.OrderStatus.CANCELADO && orderDTO.getCancellationreason() == null) {
                throw new IllegalArgumentException("Se requiere una razón de cancelación para cancelar la orden");
            }
            order.setStatus(orderDTO.getStatus());
        }

        if (orderDTO.getCancellationreason() != null) {
            if (orderDTO.getStatus() != Order.OrderStatus.CANCELADO) {
                throw new IllegalArgumentException("La razón de cancelación solo puede establecerse cuando el estado es CANCELADO");
            }
            order.setCancellationreason(orderDTO.getCancellationreason().trim());
        }

        if (orderDTO.getDeliverydate() != null) {
            // Validar que la fecha de entrega no sea anterior a la fecha de orden
            if (orderDTO.getDeliverydate().before(order.getOrderdate())) {
                throw new IllegalArgumentException("La fecha de entrega no puede ser anterior a la fecha de orden");
            }
            order.setDeliverydate(orderDTO.getDeliverydate());
        }

        if (orderDTO.getDescription() != null) {
            order.setDescription(orderDTO.getDescription().trim());
        }

        if (orderDTO.getSpecialnotes() != null) {
            order.setSpecialnotes(orderDTO.getSpecialnotes().trim());
        }

        if (orderDTO.getTotalprice() != null) {
            if (orderDTO.getTotalprice() < 0) {
                throw new IllegalArgumentException("El precio total no puede ser negativo");
            }
            order.setTotalprice(orderDTO.getTotalprice());
        }

        // 5. Asignar el cliente y el usuario a la orden
        if (orderDTO.getClientId() != null) {
            order.setClient(clientRepository.findById(orderDTO.getClientId())
                .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado")));
        }

        if (orderDTO.getUserId() != null) {
            order.setUser(userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado")));
        }

        // 6. Guardar la orden
        order = orderRepository.save(order);

        // 7. Devolver el DTO de la orden
        return mapToDTO(order);
    }

    @Override
    public Page<ShowOrderWithRatingDTO> findClientOrdersWithRatings(String clientDocumentNumber, Pageable pageable) {
        // 1. Buscar el cliente por su número de documento
        Client client = clientRepository.findByDocumentnumber(clientDocumentNumber)
            .orElseThrow(() -> new ObjectNotFoundException("Cliente no encontrado"));

        // 2. Buscar las órdenes del cliente
        Page<Order> orders = orderRepository.findByClientId(client.getId(), pageable);

        // 3. Mapear las órdenes al DTO con calificaciones
        return orders.map(order -> {
            ShowOrderWithRatingDTO dto = new ShowOrderWithRatingDTO();
            dto.setId(order.getId());
            dto.setOrdernumber(order.getOrdernumber());
            dto.setCreatedAt(order.getOrderdate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            dto.setDeliveryDate(order.getDeliverydate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
            dto.setStatus(order.getStatus());
            
            // Buscar la calificación de la orden
            Optional<Rating> rating = ratingRepository.findByOrderId(order.getId());
            if (rating.isPresent()) {
                ShowRatingDTO ratingDTO = new ShowRatingDTO();
                ratingDTO.setId(rating.get().getId());
                ratingDTO.setOrderId(order.getId());
                ratingDTO.setRating(rating.get().getRating());
                ratingDTO.setComment(rating.get().getComment());
                ratingDTO.setCreatedAt(rating.get().getCreatedAt());
                dto.setRating(ratingDTO);
            }
            
            return dto;
        });
    }

    private OrderDTO mapToDTO(Order order) {
        // 1. Crear nuevo DTO de orden
        OrderDTO orderDTO = new OrderDTO();

        // 2. Asignar los datos de la orden
        orderDTO.setOrdernumber(order.getOrdernumber());
        orderDTO.setOrderdate(order.getOrderdate());
        orderDTO.setDeliverydate(order.getDeliverydate());
        orderDTO.setDescription(order.getDescription());
        orderDTO.setSpecialnotes(order.getSpecialnotes());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setCancellationreason(order.getCancellationreason());
        orderDTO.setTotalprice(order.getTotalprice());

        // 3. Asignar el cliente y el usuario a la orden
        orderDTO.setClientId(order.getClient().getId());
        orderDTO.setUserId(order.getUser().getId());

        // 4. Mapear los detalles de la orden
        List<OrderDetailDTO> orderDetailsDTO = order.getOrderdetails().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
        orderDTO.setOrderDetails(orderDetailsDTO);

        // 5. Devolver el DTO de la orden
        return orderDTO;
    }

    private OrderDetailDTO mapToDTO(OrderDetail orderDetail) {
        // 1. Crear nuevo DTO de detalle de orden
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        // 2. Asignar los datos del detalle de orden
        orderDetailDTO.setId(orderDetail.getId());
        orderDetailDTO.setOrderId(orderDetail.getOrder().getId());
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setUnitprice(orderDetail.getUnitprice());
        orderDetailDTO.setCancellationreason(orderDetail.getCancellationreason());
        orderDetailDTO.setStructure(mapToDTO(orderDetail.getStructure()));
        
        // 3. Devolver el DTO de detalle de orden
        return orderDetailDTO;
    }

    private StructureDTO mapToDTO(Structure structure) {
        // 1. Crear nuevo DTO de estructura
        StructureDTO structureDTO = new StructureDTO();

        // 2. Asignar los datos de la estructura
        structureDTO.setId(structure.getId());
        structureDTO.setName(structure.getName());
        structureDTO.setDescription(structure.getDescription());    
        structureDTO.setColors(structure.getColors());
        structureDTO.setMaterials(structure.getMaterials());
        structureDTO.setStartdate(structure.getStartdate());
        structureDTO.setEstimatedenddate(structure.getEstimatedenddate());
        structureDTO.setRealenddate(structure.getRealenddate());
        structureDTO.setObservations(structure.getObservations());
        
        // 3. Asignar el ID del detalle de la orden si existe
        if (structure.getOrderdetail() != null) {
            structureDTO.setOrderdetailId(structure.getOrderdetail().getId());
        }

        // 4. Devolver el DTO de la estructura
        return structureDTO;
    }
}
