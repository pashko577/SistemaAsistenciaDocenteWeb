package com.example.actividades_app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.actividades_app.config.exception.RoleAlreadyExistsException;
import com.example.actividades_app.config.exception.RoleNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRoleNotFound(RoleNotFoundException ex){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "ROLE_NOT_FOUND");
        errorBody.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleRoleAlreadyExists(RoleAlreadyExistsException ex){
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("error", "ROLE_ALREDY_EXISTS");
        errorBody.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorBody);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntime(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
}
