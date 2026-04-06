package com.example.actividades_app.service;

import java.math.BigDecimal;
import java.util.List;

import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoResponseDTO;

public interface AdelantoService {


    // Para el registro manual de un adelanto
    AdelantoResponseDTO registrar(AdelantoRequestDTO request);

    // Agregar a la interfaz
AdelantoResponseDTO actualizar(Long id, AdelantoRequestDTO request);
    
    // Fundamental para el Módulo de Pagos: Trae lo que se debe descontar
    List<AdelantoResponseDTO> listarPendientesPorUsuario(Long usuarioId);
    
    // Calcula la suma total de adelantos a descontar este mes
    BigDecimal calcularTotalPendiente(Long usuarioId);
    
    // Para cuando se procesa la planilla final
    void aplicarAdelantosAPago(Long usuarioId, Long pagoId);
    
    List<AdelantoResponseDTO> listarTodos();
    
    void anularAdelanto(Long id);
}
