package com.api.server.controller.structure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.structure.CreateDesignRequest;
import com.api.server.dto.structure.DesignResponse;
import com.api.server.dto.structure.UpdateDesignRequest;
import com.api.server.service.structure.DesignService;

import java.util.List;

@RestController
@RequestMapping("/designs")
public class DesignController {

    @Autowired
    private DesignService designService;

    @PostMapping
    public ResponseEntity<DesignResponse> createDesign(
        @RequestPart("request") CreateDesignRequest request,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles
    ) {
        try {
            DesignResponse designResponse = designService.createDesign(request, imageFiles);
            return ResponseEntity.status(HttpStatus.CREATED).body(designResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DesignResponse> updateDesign(
        @PathVariable Long id,
        @RequestPart("request") UpdateDesignRequest request,
        @RequestPart(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
        @RequestPart(value = "existingImagePaths", required = false) String existingImagePaths
    ) {
        try {
            DesignResponse designResponse = designService.updateDesign(id, request, imageFiles, existingImagePaths);
            return ResponseEntity.status(HttpStatus.OK).body(designResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignResponse> getDesignById(@PathVariable Long id) {
        try {
            DesignResponse designResponse = designService.getDesignById(id);
            return ResponseEntity.status(HttpStatus.OK).body(designResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/structure/{structureId}")
    public ResponseEntity<DesignResponse> getDesignByStructureId(@PathVariable Long structureId) {
        try {
            DesignResponse designResponse = designService.getDesignByStructureId(structureId);
            return ResponseEntity.status(HttpStatus.OK).body(designResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDesign(@PathVariable Long id) {
        try {
            designService.deleteDesign(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
