package com.api.server.util;

import com.api.server.dto.security.RegisteredUser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class UserExcelExporter {
    
    public byte[] export(List<RegisteredUser> users) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Usuarios");
            
            Font arialNarrowFont = workbook.createFont();
            arialNarrowFont.setFontName("Arial Narrow");
            arialNarrowFont.setFontHeightInPoints((short) 12);
            
            // Estilo para el encabezado
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            // Agregar bordes al estilo del encabezado
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            Font headerFont = workbook.createFont();
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setBold(true);
            headerFont.setFontName("Arial Narrow");
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            
            // Estilo para las celdas de datos
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.CENTER); // Centrar contenido
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setFont(arialNarrowFont);
            
            // Agregar bordes al estilo de datos
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            sheet.createRow(0);
            
            // Crear encabezados en la segunda fila
            Row headerRow = sheet.createRow(1);
            headerRow.setHeightInPoints(30); // Altura del encabezado
            
            String[] columns = {"Nombres", "Apellidos", "Usuario", "Email", "Contacto", "Rol", "Estado"};
            
            int[] columnWidths = {20, 20, 15, 30, 15, 15, 12};
            
            for (int i = 0; i < columns.length; i++) {
                sheet.setColumnWidth(i, columnWidths[i] * 256); 
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Llenar datos
            int rowNum = 2; 
            for (RegisteredUser user : users) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(25); 
                
                Cell cell = row.createCell(0);
                cell.setCellValue(user.getName() != null ? user.getName() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(1);
                cell.setCellValue(user.getLastname() != null ? user.getLastname() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(2);
                cell.setCellValue(user.getUsername() != null ? user.getUsername() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(3);
                cell.setCellValue(user.getEmail() != null ? user.getEmail() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(4);
                cell.setCellValue(user.getContacto() != null ? user.getContacto() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(5);
                cell.setCellValue(user.getRole() != null ? user.getRole() : "");
                cell.setCellStyle(dataStyle);
                
                cell = row.createCell(6);
                String estado = "";
                if (user.getStatus() != null) {
                    estado = user.getStatus().equals("ENABLED") ? "Activo" : "Inactivo";
                }
                cell.setCellValue(estado);
                cell.setCellStyle(dataStyle);
            }
            
            workbook.write(out);
            return out.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el Excel: " + e.getMessage());
        }
    }
} 