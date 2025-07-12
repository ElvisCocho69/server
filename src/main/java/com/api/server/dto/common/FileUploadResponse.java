package com.api.server.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUploadResponse {
    private String fileName;
    private String originalFileName;
    private String fileUrl;
    private Long fileSize;
    private String contentType;
    private String category;
}
