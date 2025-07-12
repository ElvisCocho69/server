package com.api.server.controller.structure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.structure.milestone.CreateMilestoneRequest;
import com.api.server.dto.structure.milestone.UpdateMilestoneRequest;
import com.api.server.persistence.entity.structure.Milestone;
import com.api.server.service.structure.MilestoneService;

@RestController
@RequestMapping("/milestones")
public class MilestoneController {

    @Autowired
    private MilestoneService milestoneService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createMilestone(
            @RequestPart("request") CreateMilestoneRequest request,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles) {
        try {
            Milestone createdMilestone = milestoneService.createMilestone(request, imageFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMilestone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/structure/{structureId}")
    public ResponseEntity<Object> getMilestonesByStructure(
            @PathVariable Long structureId,
            Pageable pageable) {
        try {
            Page<Milestone> milestones = milestoneService.readMilestonesByStructure(structureId, pageable);
            return ResponseEntity.status(HttpStatus.OK).body(milestones);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateMilestone(
            @PathVariable Long id,
            @RequestPart("request") UpdateMilestoneRequest request,
            @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "existingImagePaths", required = false) String existingImagePaths) {
        try {
            Milestone updatedMilestone = milestoneService.updateMilestone(id, request, imageFiles, existingImagePaths);
            return ResponseEntity.status(HttpStatus.OK).body(updatedMilestone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
