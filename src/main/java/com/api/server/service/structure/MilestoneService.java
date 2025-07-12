package com.api.server.service.structure;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.persistence.entity.structure.Milestone;

import com.api.server.dto.structure.milestone.CreateMilestoneRequest;
import com.api.server.dto.structure.milestone.UpdateMilestoneRequest;

public interface MilestoneService {

    Milestone createMilestone(CreateMilestoneRequest request, List<MultipartFile> imageFiles);

    Page<Milestone> readMilestonesByStructure(Long structureId, Pageable pageable);

    Milestone updateMilestone(Long id, UpdateMilestoneRequest request, List<MultipartFile> imageFiles, String existingImagePaths);

}