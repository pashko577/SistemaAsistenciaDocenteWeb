package com.example.actividades_app.config;
import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
// Centralizamos los 3 roles aquí
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINISTRATIVO', 'ROLE_DOCENTE')")
public @interface IsStaff {
}