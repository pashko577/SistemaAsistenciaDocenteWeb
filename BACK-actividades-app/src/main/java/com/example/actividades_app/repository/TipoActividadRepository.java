package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.actividades_app.enums.TipoPlanilla;
import com.example.actividades_app.model.Entity.TipoActividad;

public interface TipoActividadRepository extends JpaRepository<TipoActividad, Long> {

    List<TipoActividad> findByTipoPlanilla(TipoPlanilla tipoPlanilla);

}