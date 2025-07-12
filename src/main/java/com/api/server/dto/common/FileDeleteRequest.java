package com.api.server.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDeleteRequest {
    private String fileName;
    private String category;
}
