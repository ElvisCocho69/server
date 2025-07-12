package com.api.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.dto.structure.milestone.CreateMilestoneRequest;
import com.api.server.dto.structure.milestone.UpdateMilestoneRequest;
import com.api.server.persistence.entity.structure.Milestone;
import com.api.server.persistence.entity.structure.Structure;
import com.api.server.persistence.repository.structure.MilestoneRepository;
import com.api.server.persistence.repository.structure.StructureRepository;
import com.api.server.service.common.FileService;
import com.api.server.service.structure.MilestoneService;

@Service
public class MilestoneServiceImpl implements MilestoneService{

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Autowired
    private StructureRepository structureRepository;

    @Autowired
    private FileService fileService;

    @Override
    public Milestone createMilestone(CreateMilestoneRequest request, List<MultipartFile> imageFiles){
        Structure structure = structureRepository.findById(request.getStructureId()).orElseThrow(() -> new RuntimeException("Estructura no encontrada"));

        Milestone milestone = new Milestone();
        milestone.setDescription(request.getDescription());
        milestone.setDate(request.getDate());

        // Procesar múltiples imágenes
        if (imageFiles != null && !imageFiles.isEmpty()) {
            List<String> imagePaths = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "milestones");
                    imagePaths.add(fileUploadResponse.getFileUrl());
                }
            }
            // Guardar las rutas de las imágenes como JSON
            milestone.setImagepath(String.join(",", imagePaths));
        }
        
        milestone.setStage(request.getStage());
        milestone.setStructure(structure);
        
        return milestoneRepository.save(milestone);
    }

    @Override
    public Page<Milestone> readMilestonesByStructure(Long structureId, Pageable pageable) {
        Page<Milestone> milestones = milestoneRepository.findAllByStructureId(structureId, pageable);
        return milestones;
    }

    @Override
    public Milestone updateMilestone(Long id, UpdateMilestoneRequest request, List<MultipartFile> imageFiles, String existingImagePaths) {
        Milestone milestone = milestoneRepository.findById(id).orElseThrow(() -> new RuntimeException("Hito no encontrado"));

        if (request.getDescription() != null && !request.getDescription().equals(milestone.getDescription())) {
            milestone.setDescription(request.getDescription());
        }

        if (request.getDate() != null && !request.getDate().equals(milestone.getDate())){
            milestone.setDate(request.getDate());
        }

        // Obtener las rutas de las imágenes actuales antes de la actualización
        List<String> oldImagePaths = new ArrayList<>();
        if (milestone.getImagepath() != null && !milestone.getImagepath().isEmpty()) {
            oldImagePaths.addAll(Arrays.asList(milestone.getImagepath().split(",")));
        }

        // Procesar imágenes existentes y nuevas
        List<String> allImagePaths = new ArrayList<>();
        
        // Agregar las imágenes existentes que se mantienen
        List<String> currentExistingImagePaths = new ArrayList<>();
        if (existingImagePaths != null && !existingImagePaths.isEmpty()) {
            currentExistingImagePaths.addAll(Arrays.asList(existingImagePaths.split(",")));
            allImagePaths.addAll(currentExistingImagePaths);
        }
        
        // Eliminar imágenes antiguas que ya no están presentes
        for (String oldPath : oldImagePaths) {
            if (!currentExistingImagePaths.contains(oldPath)) {
                fileService.deleteFile(oldPath);
            }
        }
        
        // Procesar las nuevas imágenes
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile imageFile : imageFiles) {
                if (imageFile != null && !imageFile.isEmpty()) {
                    FileUploadResponse fileUploadResponse = fileService.uploadFile(imageFile, "milestones");
                    allImagePaths.add(fileUploadResponse.getFileUrl());
                }
            }
        }
        
        // Actualizar el imagepath con todas las rutas
        if (!allImagePaths.isEmpty()) {
            milestone.setImagepath(String.join(",", allImagePaths));
        } else {
            milestone.setImagepath(null); // Clear image path if no images are present
        }

        return milestoneRepository.save(milestone);        
    }
}
