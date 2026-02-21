package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;
import com.example.actividades_app.service.TipoDocumentoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/TipoDocumento")
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService tipoDocumentoService;

    @PostMapping
    public ResponseEntity<TipoDocumento> crearTipoDocumento(@Valid @RequestBody TipoDocumentoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDocumentoService.crearTipoDocumento(dto));
    }
    
}
