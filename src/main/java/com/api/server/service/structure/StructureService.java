package com.api.server.service.structure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.api.server.dto.structure.StructureDTO;

public interface StructureService {

    Page<StructureDTO> findAll(Long orderId, Pageable pageable);

    StructureDTO findStructureById(Long id);

    StructureDTO updateStructure(Long id, StructureDTO structureDTO);

}
