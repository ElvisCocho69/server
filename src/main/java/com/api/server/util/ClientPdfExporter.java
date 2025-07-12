package com.api.server.util;

import com.api.server.dto.client.ShowClientDTO;
import com.api.server.persistence.entity.client.Client;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.Color;

public class ClientPdfExporter {
    
    public byte[] export(List<ShowClientDTO> clients) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);
            
            document.open();
            
            // Separar clientes por tipo
            List<ShowClientDTO> naturalClients = clients.stream()
                .filter(c -> c.getClientType().toString().equals("NATURAL"))
                .collect(Collectors.toList());
                
            List<ShowClientDTO> juridicoClients = clients.stream()
                .filter(c -> c.getClientType().toString().equals("JURIDICO"))
                .collect(Collectors.toList());
            
            // Agregar título principal
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            titleFont.setSize(18);
            
            Paragraph title = new Paragraph("Lista de Clientes", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));
            
            // Exportar clientes naturales
            if (!naturalClients.isEmpty()) {
                exportNaturalClients(document, naturalClients);
            }
            
            // Agregar espacio entre tablas
            document.add(new Paragraph(" "));
            
            // Exportar clientes jurídicos
            if (!juridicoClients.isEmpty()) {
                exportJuridicoClients(document, juridicoClients);
            }
            
            document.close();
            return out.toByteArray();
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }
    
    private void exportNaturalClients(Document document, List<ShowClientDTO> clients) throws DocumentException {
        // Título de la sección
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        sectionFont.setSize(14);
        Paragraph sectionTitle = new Paragraph("Clientes Naturales", sectionFont);
        sectionTitle.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(sectionTitle);
        document.add(new Paragraph(" "));
        
        // Crear tabla
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        
        float[] columnWidths = new float[]{2.0f, 2.0f, 1.7f, 2.5f, 1.5f, 2.5f, 2.5f, 1.5f};
        table.setWidths(columnWidths);
        
        // Encabezados
        writeNaturalTableHeader(table);
        
        // Datos
        writeNaturalTableData(table, clients);
        
        document.add(table);
    }
    
    private void exportJuridicoClients(Document document, List<ShowClientDTO> clients) throws DocumentException {
        // Título de la sección
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        sectionFont.setSize(14);
        Paragraph sectionTitle = new Paragraph("Clientes Jurídicos", sectionFont);
        sectionTitle.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(sectionTitle);
        document.add(new Paragraph(" "));
        
        // Crear tabla
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        
        float[] columnWidths = new float[]{3.5f, 2.0f, 3.0f, 1.5f, 2.5f, 1.5f};
        table.setWidths(columnWidths);
        
        // Encabezados
        writeJuridicoTableHeader(table);
        
        // Datos
        writeJuridicoTableData(table, clients);
        
        document.add(table);
    }
    
    private void writeNaturalTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(8, 111, 183));
        cell.setPadding(8);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);
        
        String[] headers = {
            "Nombre", "Apellido", "Documento", "Email", 
            "Contacto", "Dirección", "Fecha Nacimiento", "Estado"
        };
        
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }
    
    private void writeJuridicoTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(8, 111, 183));
        cell.setPadding(8);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);
        
        String[] headers = {
            "Razón Social", "Documento", "Email", 
            "Contacto", "Dirección", "Estado"
        };
        
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, font));
            table.addCell(cell);
        }
    }
    
    private void writeNaturalTableData(PdfPTable table, List<ShowClientDTO> clients) {
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA);
        dataFont.setSize(10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (ShowClientDTO client : clients) {
            PdfPCell cell = new PdfPCell();
            cell.setPadding(6);
            cell.setMinimumHeight(25f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            // Nombre
            cell.setPhrase(new Phrase(client.getName() != null ? client.getName() : "", dataFont));
            table.addCell(cell);
            
            // Apellido
            cell.setPhrase(new Phrase(client.getLastname() != null ? client.getLastname() : "", dataFont));
            table.addCell(cell);
            
            // Documento
            cell.setPhrase(new Phrase(client.getDocumentnumber() != null ? client.getDocumentnumber() : "", dataFont));
            table.addCell(cell);
            
            // Email
            cell.setPhrase(new Phrase(client.getEmail() != null ? client.getEmail() : "", dataFont));
            table.addCell(cell);
            
            // Contacto
            cell.setPhrase(new Phrase(client.getContact() != null ? client.getContact() : "", dataFont));
            table.addCell(cell);
            
            // Dirección
            cell.setPhrase(new Phrase(client.getAddress() != null ? client.getAddress() : "", dataFont));
            table.addCell(cell);
            
            // Fecha Nacimiento
            cell.setPhrase(new Phrase(
                client.getBirthdate() != null ? dateFormat.format(client.getBirthdate()) : "", 
                dataFont
            ));
            table.addCell(cell);
            
            // Estado
            String estado = client.getStatus() != null && 
                          client.getStatus() == Client.ClientStatus.ENABLED ? "Activo" : "Inactivo";
            cell.setPhrase(new Phrase(estado, dataFont));
            table.addCell(cell);
        }
    }
    
    private void writeJuridicoTableData(PdfPTable table, List<ShowClientDTO> clients) {
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA);
        dataFont.setSize(10);
        
        for (ShowClientDTO client : clients) {
            PdfPCell cell = new PdfPCell();
            cell.setPadding(6);
            cell.setMinimumHeight(25f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            // Razón Social
            cell.setPhrase(new Phrase(client.getRazonsocial() != null ? client.getRazonsocial() : "", dataFont));
            table.addCell(cell);
            
            // Documento
            cell.setPhrase(new Phrase(client.getDocumentnumber() != null ? client.getDocumentnumber() : "", dataFont));
            table.addCell(cell);
            
            // Email
            cell.setPhrase(new Phrase(client.getEmail() != null ? client.getEmail() : "", dataFont));
            table.addCell(cell);
            
            // Contacto
            cell.setPhrase(new Phrase(client.getContact() != null ? client.getContact() : "", dataFont));
            table.addCell(cell);
            
            // Dirección
            cell.setPhrase(new Phrase(client.getAddress() != null ? client.getAddress() : "", dataFont));
            table.addCell(cell);
            
            // Estado
            String estado = client.getStatus() != null && 
                          client.getStatus() == Client.ClientStatus.ENABLED ? "Activo" : "Inactivo";
            cell.setPhrase(new Phrase(estado, dataFont));
            table.addCell(cell);
        }
    }
} 