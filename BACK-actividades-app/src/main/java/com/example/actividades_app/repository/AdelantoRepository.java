package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.EstadoAdelanto;
import com.example.actividades_app.model.Entity.Adelanto;

@Repository
public interface AdelantoRepository extends JpaRepository<Adelanto, Long> {

// Busca adelantos pendientes para restarlos en la planilla
    List<Adelanto> findByUsuarioIdAndEstado(Long usuarioId, EstadoAdelanto estado);
    
    List<Adelanto> findByPagoId(Long pagoId);
    
    boolean existsByPagoId(Long pagoId);
}