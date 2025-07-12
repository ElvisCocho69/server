package com.api.server.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.server.dto.common.FileUploadResponse;
import com.api.server.service.common.FileService;
import com.api.server.util.common.FileValidator;
import com.api.server.util.common.FileUtils;

@Service
public class FileServiceImpl implements FileService {

    @Value("${app.file.upload.base-directory:uploads}")
    private String baseUploadDirectory;

    private final FileValidator fileValidator;
    private final FileUtils fileUtils;

    public FileServiceImpl(FileValidator fileValidator, FileUtils fileUtils) {
        this.fileValidator = fileValidator;
        this.fileUtils = fileUtils;
    }

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, String category) {
        // Validar archivo
        fileValidator.validateFile(file, category);

        try {
            // Generar nombre único para el archivo
            String fileName = generateUniqueFileName(file);

            // Crear la ruta de la categoría
            Path categoryPath = Paths.get(baseUploadDirectory, category);
            Files.createDirectories(categoryPath);

            // Ruta completa del archivo
            Path filePath = categoryPath.resolve(fileName);

            // Guardar el archivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Construir URL de acceso
            String fileUrl = String.format("/files/%s/%s", category, fileName);

            return FileUploadResponse.builder()
                .fileName(fileName)
                .originalFileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .category(category)
                .build();
        } catch (IOException e) {
            throw new RuntimeException("Error al subir el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        try {
            // Extraer la categoría y el nombre del archivo de la ruta completa
            String[] parts = filePath.split("/");
            String category = parts[parts.length - 2];
            String fileName = parts[parts.length - 1];

            Path fullPath = Paths.get(baseUploadDirectory, category, fileName);
            if (!Files.exists(fullPath)) {
                throw new RuntimeException("El archivo no existe: " + filePath);
            }
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource getFile(String fileName, String category) {
        try {
            Path filePath = Paths.get(baseUploadDirectory, category).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("No se pudo encontrar el archivo: " + fileName);
            }

            if (!resource.isReadable()) {
                throw new RuntimeException("No se puede leer el archivo: " + fileName);
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el archivo: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean fileExists(String fileName, String category) {
        try {
            Path filePath = Paths.get(baseUploadDirectory, category, fileName);
            return Files.exists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar la existencia del archivo: " + e.getMessage(), e);
        }
    }

    private String generateUniqueFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = fileUtils.getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
    }
}