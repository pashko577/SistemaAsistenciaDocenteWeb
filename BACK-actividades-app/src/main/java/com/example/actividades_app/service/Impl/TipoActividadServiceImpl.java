package com.example.actividades_app.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.actividades_app.enums.TipoPlanilla;
import com.example.actividades_app.model.Entity.TipoActividad;
import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;
import com.example.actividades_app.repository.TipoActividadRepository;
import com.example.actividades_app.service.TipoActividadService;

@Service
@RequiredArgsConstructor
public class TipoActividadServiceImpl implements TipoActividadService {

        private final TipoActividadRepository tipoActividadRepository;

        @Override
        public TipoActividadResponseDTO crear(TipoActividadRequestDTO dto) {

                TipoActividad tipo = TipoActividad.builder()
                                .nombre(dto.getNombre())
                                .tipoPlanilla(dto.getTipoPlanilla())
                                .build();

                tipoActividadRepository.save(tipo);

                return TipoActividadResponseDTO.builder()
                                .id(tipo.getId())
                                .nombre(tipo.getNombre())
                                .tipoPlanilla(tipo.getTipoPlanilla())
                                .build();
        }

        // En TipoActividadServiceImpl.java

        @Override
        public TipoActividadResponseDTO actualizar(Long id, TipoActividadRequestDTO dto) {
                TipoActividad tipo = tipoActividadRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));

                tipo.setNombre(dto.getNombre());
                tipo.setTipoPlanilla(dto.getTipoPlanilla());
                tipoActividadRepository.save(tipo);

                return mapToDTO(tipo);
        }

        @Override
        public void eliminar(Long id) {
                // Es recomendable validar si hay contratos usando esta actividad antes de
                // borrar
                tipoActividadRepository.deleteById(id);
        }

        @Override
        public TipoActividadResponseDTO buscarPorId(Long id) {
                return tipoActividadRepository.findById(id)
                                .map(this::mapToDTO)
                                .orElseThrow(() -> new RuntimeException("Tipo de actividad no encontrado"));
        }

        @Override
public List<TipoActividadResponseDTO> listar() {
    return tipoActividadRepository.findAll()
            .stream()
            .map(this::mapToDTO) // Usamos el método auxiliar que ya creaste
            .collect(Collectors.toList());
}
        @Override
        public List<TipoActividadResponseDTO> listarPorPlanilla(TipoPlanilla planilla) {
                return tipoActividadRepository.findByTipoPlanilla(planilla)
                                .stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        // Método auxiliar para no repetir código (DRY)
        private TipoActividadResponseDTO mapToDTO(TipoActividad tipo) {
                return TipoActividadResponseDTO.builder()
                                .id(tipo.getId())
                                .nombre(tipo.getNombre())
                                .tipoPlanilla(tipo.getTipoPlanilla())
                                .build();
        }
}