package com.api.server.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserInactiveException extends RuntimeException {

    public UserInactiveException(String message) {
        super(message);
    }

    public UserInactiveException(String message, Throwable cause) {
        super(message, cause);
    }
} 