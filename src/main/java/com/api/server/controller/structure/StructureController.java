package com.api.server.controller.structure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.server.dto.structure.StructureDTO;
import com.api.server.service.structure.StructureService;

@RestController
@RequestMapping("/structures")
public class StructureController {

    @Autowired
    private StructureService structureService;

    @GetMapping
    public ResponseEntity<Page<StructureDTO>> listAllStructures(
        @RequestParam(required = false) Long orderId,
        Pageable pageable
    ) {
        try {
            Page<StructureDTO> structures = structureService.findAll(orderId, pageable);
            return ResponseEntity.ok(structures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<StructureDTO> findStructureById(@PathVariable Long id) {
        StructureDTO structure = structureService.findStructureById(id);
        return ResponseEntity.ok(structure);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StructureDTO> updateStructure(
        @PathVariable Long id, 
        @RequestBody StructureDTO structureDTO
    ) {
        try {
            StructureDTO updatedStructure = structureService.updateStructure(id, structureDTO);
            return ResponseEntity.ok(updatedStructure);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    
}

    
