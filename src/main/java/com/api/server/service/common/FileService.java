package com.api.server.service.common;

import com.api.server.dto.common.FileUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    
    /**
     * Sube un archivo a una categoría específica
     * @param file Archivo a subir
     * @param category Categoría (designs, products, users, etc.)
     * @return Información del archivo subido
     */
    FileUploadResponse uploadFile(MultipartFile file, String category);
    
    /**
     * Elimina un archivo
     * @param fileName Nombre del archivo
     * @param category Categoría del archivo
     */
    void deleteFile(String filePath);
    
    /**
     * Obtiene un archivo
     */
    Resource getFile(String fileName, String category);
    
    /**
     * Valida si un archivo existe
     */
    boolean fileExists(String fileName, String category);
}