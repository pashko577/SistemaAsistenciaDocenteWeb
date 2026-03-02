package com.example.actividades_app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.actividades_app.config.exception.RoleAlreadyExistsException;
import com.example.actividades_app.config.exception.RoleNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja errores de Negocio lanzados como RuntimeException (DNI duplicado, Email duplicado, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage()); 
        // Devolvemos un JSON: {"message": "El DNI ya se encuentra registrado"}
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRoleNotFound(RoleNotFoundException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleRoleAlreadyExists(RoleAlreadyExistsException ex){
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    // OPCIONAL: Maneja errores de validación de @Valid (campos vacíos, emails inválidos)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();
        String mensaje = ex.getBindingResult().getFieldError().getDefaultMessage();
        error.put("message", mensaje);
        return ResponseEntity.badRequest().body(error);
    }
}
