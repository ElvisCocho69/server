package com.api.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.order.OrderDetailDTO;
import com.api.server.dto.structure.StructureDTO;
import com.api.server.exception.ObjectNotFoundException;
import com.api.server.persistence.entity.order.OrderDetail;
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.service.order.OrderDetailService;
import com.api.server.persistence.repository.order.OrderDetailRepository;
import com.api.server.persistence.repository.order.OrderRepository;
import com.api.server.persistence.repository.structure.StructureRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StructureRepository structureRepository;

    @Override
    public Page<OrderDetailDTO> findAll(Long orderId, Pageable pageable) {
        Page<OrderDetail> orderDetails;
        if (orderId != null) {
            orderDetails = orderDetailRepository.findByOrderId(orderId, pageable);
        } else {
            orderDetails = orderDetailRepository.findAll(pageable);
        }
        return orderDetails.map(this::mapToDTO);
    }

    @Override
    public OrderDetailDTO findOrderDetailById(Long id) {
        return orderDetailRepository.findById(id)
            .map(this::mapToDTO)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la orden no encontrado"));
    }

    @Override
    public OrderDetailDTO updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        // 1. Validar el DTO
        if (orderDetailDTO == null) {
            throw new IllegalArgumentException("El detalle de la orden no puede ser nulo");
        }

        // 2. Buscar el detalle de la orden
        OrderDetail orderDetail = orderDetailRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Detalle de la orden no encontrado"));

        // 3. Actualizar el detalle de la orden con validaciones
        if (orderDetailDTO.getQuantity() != null) {
            if (orderDetailDTO.getQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            orderDetail.setQuantity(orderDetailDTO.getQuantity());
        }
        
        if (orderDetailDTO.getUnitprice() != null) {
            if (orderDetailDTO.getUnitprice() <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
            orderDetail.setUnitprice(orderDetailDTO.getUnitprice());
        }

        if (orderDetailDTO.getCancellationreason() != null) {
            orderDetail.setCancellationreason(orderDetailDTO.getCancellationreason());
        }

        // 4. Actualizar la estructura si viene en el DTO
        if (orderDetailDTO.getStructure() != null) {
            Structure structure = orderDetail.getStructure();
            StructureDTO structureDTO = orderDetailDTO.getStructure();

            if (structureDTO.getName() != null) {
                structure.setName(structureDTO.getName());
            }
            if (structureDTO.getDescription() != null) {
                structure.setDescription(structureDTO.getDescription());
            }
            if (structureDTO.getColors() != null) {
                structure.setColors(structureDTO.getColors());
            }
            if (structureDTO.getMaterials() != null) {
                structure.setMaterials(structureDTO.getMaterials());
            }
            if (structureDTO.getStartdate() != null) {
                structure.setStartdate(structureDTO.getStartdate());
            }
            if (structureDTO.getEstimatedenddate() != null) {
                structure.setEstimatedenddate(structureDTO.getEstimatedenddate());
            }
            if (structureDTO.getRealenddate() != null) {
                structure.setRealenddate(structureDTO.getRealenddate());
            }
            if (structureDTO.getObservations() != null) {
                structure.setObservations(structureDTO.getObservations());
            }

            // Guardar la estructura primero
            structure = structureRepository.save(structure);
            orderDetail.setStructure(structure);
        }

        // 5. Asignar la orden al detalle de la orden
        if (orderDetailDTO.getOrderId() != null) {
            orderDetail.setOrder(orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new ObjectNotFoundException("Orden no encontrada")));
        }

        // 6. Guardar el detalle de la orden
        orderDetail = orderDetailRepository.save(orderDetail);

        // 7. Devolver el DTO del detalle de la orden
        return mapToDTO(orderDetail);
    }

    private OrderDetailDTO mapToDTO(OrderDetail orderDetail) {
        // 1. Convertir la entidad a DTO
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

        // 2. Asignar los valores del detalle de la orden
        orderDetailDTO.setId(orderDetail.getId());
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setUnitprice(orderDetail.getUnitprice());
        orderDetailDTO.setCancellationreason(orderDetail.getCancellationreason());
        orderDetailDTO.setOrderId(orderDetail.getOrder().getId());
        orderDetailDTO.setStructure(mapToDTO(orderDetail.getStructure()));

        // 3. Devolver el DTO del detalle de la orden
        return orderDetailDTO;
    }

    private StructureDTO mapToDTO(Structure structure) {
        // 1. Convertir la entidad a DTO
        StructureDTO structureDTO = new StructureDTO();

        // 2. Asignar los valores de la estructura
        structureDTO.setId(structure.getId());
        structureDTO.setName(structure.getName());
        structureDTO.setDescription(structure.getDescription());    
        structureDTO.setColors(structure.getColors());
        structureDTO.setMaterials(structure.getMaterials());
        structureDTO.setStartdate(structure.getStartdate());
        structureDTO.setEstimatedenddate(structure.getEstimatedenddate());
        structureDTO.setRealenddate(structure.getRealenddate());
        structureDTO.setObservations(structure.getObservations());

        // 3. Devolver el DTO de la estructura
        return structureDTO;
    }

}
