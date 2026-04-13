package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoResponseDTO;
import com.example.actividades_app.service.TipoDocumentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/tipo-documento")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @PostMapping
  @PreAuthorize("permitAll()")
    public ResponseEntity<TipoDocumentoResponseDTO> crearTipoDocumento(
            @Valid @RequestBody TipoDocumentoRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoDocumentoService.crearTipoDocumento(dto));
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<TipoDocumentoResponseDTO>> listar() {
        return ResponseEntity.ok(tipoDocumentoService.listarTodas());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<TipoDocumentoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoDocumentoService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoDocumentoService.eliminarTipoDocumento(id);
        return ResponseEntity.noContent().build();
    }
}