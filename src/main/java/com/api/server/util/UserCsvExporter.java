package com.api.server.util;

import com.api.server.dto.security.RegisteredUser;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;

public class UserCsvExporter {
    
    public byte[] export(List<RegisteredUser> users) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             PrintWriter writer = new PrintWriter(out)) {
            
            // Escribir BOM para soporte de caracteres especiales en Excel
            writer.write('\ufeff');
            
            // Encabezados
            writer.println("Nombres,Apellidos,Usuario,Email,Contacto,Rol,Estado");
            
            // Datos
            for (RegisteredUser user : users) {
                writer.println(
                    escapeSpecialCharacters(user.getName()) + "," +
                    escapeSpecialCharacters(user.getLastname()) + "," +
                    escapeSpecialCharacters(user.getUsername()) + "," +
                    escapeSpecialCharacters(user.getEmail()) + "," +
                    escapeSpecialCharacters(user.getContacto()) + "," +
                    escapeSpecialCharacters(user.getRole()) + "," +
                    escapeSpecialCharacters(user.getStatus() != null ? 
                        (user.getStatus().equals("ENABLED") ? "Activo" : "Inactivo") : "")
                );
            }
            
            writer.flush();
            return out.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el CSV: " + e.getMessage());
        }
    }
    
    private String escapeSpecialCharacters(String data) {
        if (data == null) {
            return "";
        }
        String escapedData = data.replaceAll("\"", "\"\"");
        if (escapedData.contains(",") || escapedData.contains("\"") || 
            escapedData.contains("\n") || escapedData.contains("\r")) {
            return "\"" + escapedData + "\"";
        }
        return escapedData;
    }
} 