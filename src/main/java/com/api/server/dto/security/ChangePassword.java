package com.api.server.dto.security;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {
    
    @Size(min = 8)
    private String password;

    @Size(min = 8)
    private String repeatedPassword;

    private String oldPassword;
    
}
