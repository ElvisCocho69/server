package com.api.server.util;

import com.api.server.dto.client.ShowClientDTO;
import com.api.server.persistence.entity.client.Client;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class ClientExcelExporter {
    
    public byte[] export(List<ShowClientDTO> clients) {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            // Separar clientes por tipo
            List<ShowClientDTO> naturalClients = clients.stream()
                .filter(c -> c.getClientType().toString().equals("NATURAL"))
                .collect(Collectors.toList());
                
            List<ShowClientDTO> juridicoClients = clients.stream()
                .filter(c -> c.getClientType().toString().equals("JURIDICO"))
                .collect(Collectors.toList());
            
            // Crear estilos comunes
            Font arialNarrowFont = workbook.createFont();
            arialNarrowFont.setFontName("Arial Narrow");
            arialNarrowFont.setFontHeightInPoints((short) 12);
            
            CellStyle headerStyle = createHeaderStyle(workbook, arialNarrowFont);
            CellStyle dataStyle = createDataStyle(workbook, arialNarrowFont);
            
            // Exportar clientes naturales
            if (!naturalClients.isEmpty()) {
                exportNaturalClients(workbook, naturalClients, headerStyle, dataStyle);
            }
            
            // Exportar clientes jurídicos
            if (!juridicoClients.isEmpty()) {
                exportJuridicoClients(workbook, juridicoClients, headerStyle, dataStyle);
            }
            
            workbook.write(out);
            return out.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el Excel: " + e.getMessage());
        }
    }
    
    private CellStyle createHeaderStyle(Workbook workbook, Font font) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
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
        
        return headerStyle;
    }
    
    private CellStyle createDataStyle(Workbook workbook, Font font) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        dataStyle.setFont(font);
        
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        
        return dataStyle;
    }
    
    private void exportNaturalClients(Workbook workbook, List<ShowClientDTO> clients, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Clientes Naturales");
        
        // Crear fila en blanco al inicio
        sheet.createRow(0);
        
        // Crear encabezados
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(30);
        
        String[] columns = {
            "Nombre", "Apellido", "Documento", "Email", 
            "Contacto", "Dirección", "Fecha Nacimiento", "Estado"
        };
        
        int[] columnWidths = {20, 20, 20, 30, 15, 30, 15, 12};
        
        for (int i = 0; i < columns.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * 256);
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        int rowNum = 2;
        
        for (ShowClientDTO client : clients) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(25);
            
            int colNum = 0;
            
            // Nombre
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(client.getName() != null ? client.getName() : "");
            cell.setCellStyle(dataStyle);
            
            // Apellido
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getLastname() != null ? client.getLastname() : "");
            cell.setCellStyle(dataStyle);
            
            // Documento
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getDocumentnumber() != null ? client.getDocumentnumber() : "");
            cell.setCellStyle(dataStyle);
            
            // Email
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getEmail() != null ? client.getEmail() : "");
            cell.setCellStyle(dataStyle);
            
            // Contacto
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getContact() != null ? client.getContact() : "");
            cell.setCellStyle(dataStyle);
            
            // Dirección
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getAddress() != null ? client.getAddress() : "");
            cell.setCellStyle(dataStyle);
            
            // Fecha Nacimiento
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getBirthdate() != null ? dateFormat.format(client.getBirthdate()) : "");
            cell.setCellStyle(dataStyle);
            
            // Estado
            cell = row.createCell(colNum++);
            String estado = client.getStatus() != null && 
                          client.getStatus() == Client.ClientStatus.ENABLED ? "Activo" : "Inactivo";
            cell.setCellValue(estado);
            cell.setCellStyle(dataStyle);
        }
    }
    
    private void exportJuridicoClients(Workbook workbook, List<ShowClientDTO> clients, CellStyle headerStyle, CellStyle dataStyle) {
        Sheet sheet = workbook.createSheet("Clientes Jurídicos");
        
        // Crear fila en blanco al inicio
        sheet.createRow(0);
        
        // Crear encabezados
        Row headerRow = sheet.createRow(1);
        headerRow.setHeightInPoints(30);
        
        String[] columns = {
            "Razón Social", "Documento", "Email", 
            "Contacto", "Dirección", "Estado"
        };
        
        int[] columnWidths = {40, 20, 30, 15, 30, 12};
        
        for (int i = 0; i < columns.length; i++) {
            sheet.setColumnWidth(i, columnWidths[i] * 256);
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Llenar datos
        int rowNum = 2;
        
        for (ShowClientDTO client : clients) {
            Row row = sheet.createRow(rowNum++);
            row.setHeightInPoints(25);
            
            int colNum = 0;
            
            // Razón Social
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(client.getRazonsocial() != null ? client.getRazonsocial() : "");
            cell.setCellStyle(dataStyle);
            
            // Documento
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getDocumentnumber() != null ? client.getDocumentnumber() : "");
            cell.setCellStyle(dataStyle);
            
            // Email
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getEmail() != null ? client.getEmail() : "");
            cell.setCellStyle(dataStyle);
            
            // Contacto
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getContact() != null ? client.getContact() : "");
            cell.setCellStyle(dataStyle);
            
            // Dirección
            cell = row.createCell(colNum++);
            cell.setCellValue(client.getAddress() != null ? client.getAddress() : "");
            cell.setCellStyle(dataStyle);
            
            // Estado
            cell = row.createCell(colNum++);
            String estado = client.getStatus() != null && 
                          client.getStatus() == Client.ClientStatus.ENABLED ? "Activo" : "Inactivo";
            cell.setCellValue(estado);
            cell.setCellStyle(dataStyle);
        }
    }
} 