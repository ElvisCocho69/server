package com.api.server.util;

import com.api.server.dto.security.RegisteredUser;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.awt.Color;

public class UserPdfExporter {
    
    public byte[] export(List<RegisteredUser> users) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
            PdfWriter.getInstance(document, out);
            
            document.open();
            
            // Agregar título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            titleFont.setSize(18);
            
            Paragraph title = new Paragraph("Lista de Usuarios", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); 
            
            // Columnas
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100f);
            table.setSpacingBefore(10);
            
            float[] columnWidths = new float[]{2.3f, 2.3f, 2f, 3.0f, 1.5f, 2.0f, 1.5f}; 
            table.setWidths(columnWidths);
            
            // Encabezados
            writeTableHeader(table);
            
            // Datos
            writeTableData(table, users);
            
            document.add(table);
            document.close();
            
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage());
        }
    }
    
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(new Color(8, 111, 183));
        cell.setPadding(8);
        cell.setMinimumHeight(30f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE);
        
        cell.setPhrase(new Phrase("Nombres", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Apellidos", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Usuario", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Contacto", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Rol", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Estado", font));
        table.addCell(cell);
    }
    
    private void writeTableData(PdfPTable table, List<RegisteredUser> users) {
        Font dataFont = FontFactory.getFont(FontFactory.HELVETICA);
        dataFont.setSize(10);
        
        for (RegisteredUser user : users) {
            PdfPCell cell = new PdfPCell();
            cell.setPadding(6);
            cell.setMinimumHeight(25f);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            cell.setPhrase(new Phrase(user.getName() != null ? user.getName() : "", dataFont));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase(user.getLastname() != null ? user.getLastname() : "", dataFont));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase(user.getUsername() != null ? user.getUsername() : "", dataFont));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase(user.getEmail() != null ? user.getEmail() : "", dataFont));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase(user.getContacto() != null ? user.getContacto() : "", dataFont));
            table.addCell(cell);
            
            cell.setPhrase(new Phrase(user.getRole() != null ? user.getRole() : "", dataFont));
            table.addCell(cell);
            
            String estado = "";
            if (user.getStatus() != null) {
                estado = user.getStatus().equals("ENABLED") ? "Activo" : "Inactivo";
            }
            cell.setPhrase(new Phrase(estado, dataFont));
            table.addCell(cell);
        }
    }
} 