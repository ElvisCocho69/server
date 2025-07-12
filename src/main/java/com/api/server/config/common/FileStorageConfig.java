package com.api.server.config.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app.file")
@Getter
@Setter
public class FileStorageConfig {

    private Upload upload = new Upload();

    @Getter
    @Setter
    public static class Upload {
        private String baseDirectory = "uploads";
        private long maxFileSize = 200 * 1024 * 1024; // 200MB
        private Map<String, CategoryConfig> categories;        
    }

    @Getter
    @Setter
    public static class CategoryConfig {
        private List<String> allowedTypes;
        private long maxSize;
        private boolean requireAuthentication = false;
    }

}
