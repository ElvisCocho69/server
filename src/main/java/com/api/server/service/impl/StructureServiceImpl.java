package com.api.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.api.server.dto.structure.StructureDTO;
import com.api.server.exception.ObjectNotFoundException;    
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.persistence.repository.structure.StructureRepository;
import com.api.server.service.structure.StructureService;

@Service
public class StructureServiceImpl implements StructureService {

    @Autowired
    private StructureRepository structureRepository;


    @Override
    public Page<StructureDTO> findAll(Long orderId, Pageable pageable) {
        Page<Structure> structures;
        if (orderId != null) {
            structures = structureRepository.findByOrderdetailOrderId(orderId, pageable);
        } else {
            structures = structureRepository.findAll(pageable);
        }
        return structures.map(this::mapToDTO);
    }

    @Override
    public StructureDTO findStructureById(Long id) {
        return structureRepository.findById(id).map(this::mapToDTO).orElseThrow(() -> new ObjectNotFoundException("Estructura no encontrada"));
    }

    @Override
    public StructureDTO updateStructure(Long id, StructureDTO structureDTO) {
        // 1. Validar el DTO
        if (structureDTO == null) {
            throw new IllegalArgumentException("La estructura no puede ser nula");
        }

        // 2. Buscar la estructura existente
        Structure structure = structureRepository.findById(id)
            .orElseThrow(() -> new ObjectNotFoundException("Estructura no encontrada"));
        
        // 3. Validar campos requeridos
        if (structureDTO.getName() == null || structureDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la estructura es requerido");
        }

        if (structureDTO.getOrderdetailId() == null) {
            throw new IllegalArgumentException("El ID del detalle de orden es requerido");
        }

        // 4. Validar fechas
        if (structureDTO.getStartdate() != null && structureDTO.getEstimatedenddate() != null) {
            if (structureDTO.getStartdate().after(structureDTO.getEstimatedenddate())) {
                throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha estimada de finalización");
            }
        }

        if (structureDTO.getRealenddate() != null) {
            if (structureDTO.getStartdate() != null && structureDTO.getRealenddate().before(structureDTO.getStartdate())) {
                throw new IllegalArgumentException("La fecha real de finalización no puede ser anterior a la fecha de inicio");
            }
        }

        // 5. Actualizar los campos
        structure.setName(structureDTO.getName().trim());
        structure.setDescription(structureDTO.getDescription() != null ? structureDTO.getDescription().trim() : null);
        structure.setColors(structureDTO.getColors());
        structure.setMaterials(structureDTO.getMaterials());
        structure.setStartdate(structureDTO.getStartdate());
        structure.setEstimatedenddate(structureDTO.getEstimatedenddate());
        structure.setRealenddate(structureDTO.getRealenddate());
        structure.setObservations(structureDTO.getObservations() != null ? structureDTO.getObservations().trim() : null);
        
        // 6. Guardar los cambios
        Structure updatedStructure = structureRepository.save(structure);
        return mapToDTO(updatedStructure);
    }

    private StructureDTO mapToDTO(Structure structure) {
        StructureDTO structureDTO = new StructureDTO();
        structureDTO.setId(structure.getId());
        structureDTO.setName(structure.getName());
        structureDTO.setDescription(structure.getDescription());
        structureDTO.setColors(structure.getColors());
        structureDTO.setMaterials(structure.getMaterials());
        structureDTO.setStartdate(structure.getStartdate());
        structureDTO.setEstimatedenddate(structure.getEstimatedenddate());
        structureDTO.setRealenddate(structure.getRealenddate());
        structureDTO.setObservations(structure.getObservations());
        structureDTO.setOrderdetailId(structure.getOrderdetail().getId());
        
        return structureDTO;
    }
}
