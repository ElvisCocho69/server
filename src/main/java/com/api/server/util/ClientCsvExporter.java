package com.api.server.util;

import com.api.server.dto.client.ShowClientDTO;
import com.api.server.persistence.entity.client.Client;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ClientCsvExporter {
    private static final String[] HEADERS = {
        "Nombre Completo",
        "Tipo",
        "Documento",
        "Email",
        "Contacto",
        "Estado"
    };

    private String getFullName(ShowClientDTO client) {
        if (client.getClientType().toString().equals("NATURAL")) {
            return client.getName() + " " + client.getLastname();
        } else {
            return client.getRazonsocial();
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    public byte[] export(List<ShowClientDTO> clients) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8)) {

            // Escribir BOM para Excel
            outputStream.write(0xEF);
            outputStream.write(0xBB);
            outputStream.write(0xBF);

            // Escribir encabezados
            writer.write(String.join(",", HEADERS));
            writer.write("\n");

            // Escribir datos
            for (ShowClientDTO client : clients) {
                String estado = client.getStatus() != null && 
                              client.getStatus() == Client.ClientStatus.ENABLED ? "Activo" : "Inactivo";
                              
                String[] row = {
                    escapeCsv(getFullName(client)),
                    escapeCsv(client.getClientType().toString()),
                    escapeCsv(client.getDocumentnumber()),
                    escapeCsv(client.getEmail()),
                    escapeCsv(client.getContact()),
                    escapeCsv(estado)
                };
                writer.write(String.join(",", row));
                writer.write("\n");
            }

            writer.flush();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error al exportar a CSV: " + e.getMessage());
        }
    }
} 