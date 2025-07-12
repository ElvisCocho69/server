package com.api.server.dto.security;

import java.util.Collection;

import com.api.server.persistence.entity.security.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String contacto;
    private String username;
    private RoleDTO role;
    private Collection<String> authorities;
    private String status;

    @Getter
    @Setter
    public static class RoleDTO {
        private String name;
    }

    public static UserProfileDTO fromUser(User user) {
        UserProfileDTO dto = new UserProfileDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastname(user.getLastname());
        dto.setEmail(user.getEmail());
        dto.setContacto(user.getContacto());
        dto.setUsername(user.getUsername());
        dto.setStatus(user.getStatus() != null ? user.getStatus().name() : null);
        
        // Configurar el rol
        if (user.getRole() != null) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(user.getRole().getName());
            dto.setRole(roleDTO);
        }
        
        // Convertir authorities a lista de strings
        if (user.getAuthorities() != null) {
            dto.setAuthorities(user.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList());
        }
        
        return dto;
    }
} 