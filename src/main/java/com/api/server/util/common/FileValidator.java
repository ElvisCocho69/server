package com.api.server.util.common;

import com.api.server.config.common.FileStorageConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Component
public class FileValidator {
    
    private final FileStorageConfig config;
    
    // Tipos permitidos por defecto para cada categoría
    private static final List<String> DEFAULT_IMAGE_TYPES = 
        Arrays.asList("image/jpeg", "image/png", "image/webp");
    
    public FileValidator(FileStorageConfig config) {
        this.config = config;
    }
    
    public void validateFile(MultipartFile file, String category) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }
        
        validateFileType(file, category);
        validateFileSize(file, category);
        validateFileName(file.getOriginalFilename());
    }
    
    private void validateFileType(MultipartFile file, String category) {
        List<String> allowedTypes = getAllowedTypesForCategory(category);
        
        if (!allowedTypes.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                String.format("Tipo de archivo %s no permitido para la categoría %s", 
                             file.getContentType(), category));
        }
    }
    
    private void validateFileSize(MultipartFile file, String category) {
        long maxSize = getMaxSizeForCategory(category);
        
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                String.format("El tamaño del archivo %d excede el límite %d para la categoría %s", 
                             file.getSize(), maxSize, category));
        }
    }
    
    private void validateFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del archivo no puede estar vacío");
        }
        
        // Validar caracteres peligrosos
        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
            throw new IllegalArgumentException("El nombre del archivo no es válido");
        }
    }
    
    private List<String> getAllowedTypesForCategory(String category) {
        var categoryConfig = config.getUpload().getCategories();
        
        if (categoryConfig != null && categoryConfig.containsKey(category)) {
            return categoryConfig.get(category).getAllowedTypes();
        }
        
        return switch (category.toLowerCase()) {
            case "designs", "progress" -> DEFAULT_IMAGE_TYPES;
            default -> DEFAULT_IMAGE_TYPES;
        };
    }
    
    private long getMaxSizeForCategory(String category) {
        var categoryConfig = config.getUpload().getCategories();
        
        if (categoryConfig != null && categoryConfig.containsKey(category)) {
            return categoryConfig.get(category).getMaxSize();
        }
        
        return config.getUpload().getMaxFileSize();
    }
}