package com.api.server.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.api.server.dto.ApiError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handlerAccessDeniedException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Acceso denegado. No tienes los permisos necesarios para acceder a esta función. " +
                "Por favor, contacta al administrador si crees que esto es un error");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(HttpServletRequest request, DisabledException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage(
                "Tu cuenta está desactivada");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(HttpServletRequest request,
            UsernameNotFoundException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Credenciales Inválidas");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<?> handleUserInactiveException(HttpServletRequest request, UserInactiveException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage(
                "Tu cuenta está desactivada");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handleObjectNotFoundException(HttpServletRequest request,
            ObjectNotFoundException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("El recurso solicitado no fue encontrado");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(HttpServletRequest request,
            BadCredentialsException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Contraseña incorrecta");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(HttpServletRequest request,
            MethodArgumentNotValidException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Error de validación. Por favor verifica los datos ingresados");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(HttpServletRequest request,
            DataIntegrityViolationException exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Error de integridad de datos. Es posible que estés intentando duplicar información única");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(HttpServletRequest request, Exception exception) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Ha ocurrido un error inesperado. Por favor, inténtalo de nuevo más tarde");
        apiError.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
