package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.EstadoReemplazo;
import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.Entity.Docente;
import com.example.actividades_app.model.Entity.ReemplazoDocente;
import com.example.actividades_app.model.Entity.TipoReemplazo;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteResponseDTO;
import com.example.actividades_app.repository.CronogramaDiarioRepository;
import com.example.actividades_app.repository.DocenteRepository;
import com.example.actividades_app.repository.ReemplazoDocenteRepository;
import com.example.actividades_app.repository.TipoReemplazoRepository;
import com.example.actividades_app.service.ReemplazoDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReemplazoDocenteServiceImpl implements ReemplazoDocenteService {

    private final ReemplazoDocenteRepository reemplazoRepository;
    private final DocenteRepository docenteRepository;
    private final CronogramaDiarioRepository cronogramaRepository;
    private final TipoReemplazoRepository tipoReemplazoRepository;

    @Override
    public ReemplazoDocenteResponseDTO registrar(ReemplazoDocenteRequestDTO dto) {

        if (reemplazoRepository.existsByCronogramaDiarioId(dto.getCronogramaDiarioId())) {
            throw new RuntimeException("Ya existe un reemplazo para esta clase");
        }

        Docente titular = docenteRepository.findById(dto.getDocenteTitularId())
                .orElseThrow(() -> new RuntimeException("Docente titular no encontrado"));

        Docente reemplazo = docenteRepository.findById(dto.getDocenteReemplazoId())
                .orElseThrow(() -> new RuntimeException("Docente reemplazo no encontrado"));

        CronogramaDiario cronograma = cronogramaRepository.findById(dto.getCronogramaDiarioId())
                .orElseThrow(() -> new RuntimeException("Cronograma diario no encontrado"));

        TipoReemplazo tipo = tipoReemplazoRepository.findById(dto.getTipoReemplazoId())
                .orElseThrow(() -> new RuntimeException("Tipo reemplazo no encontrado"));

        ReemplazoDocente r = ReemplazoDocente.builder()
                .docenteTitular(titular)
                .docenteReemplazo(reemplazo)
                .cronogramaDiario(cronograma)
                .motivo(dto.getMotivo())
                .tipoReemplazo(tipo)
                .estado(EstadoReemplazo.PENDIENTE)
                .fechaRegistro(LocalDate.now())
                .build();

        reemplazoRepository.save(r);

        return mapToResponse(r);
    }

    @Override
    public List<ReemplazoDocenteResponseDTO> listar() {

        return reemplazoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReemplazoDocenteResponseDTO mapToResponse(ReemplazoDocente r) {

        return ReemplazoDocenteResponseDTO.builder()
                .id(r.getId())
                .docenteTitularId(r.getDocenteTitular().getId())
                .docenteTitularNombre(r.getDocenteTitular().getUsuario().getPersona().getNombres())
                .docenteReemplazoId(r.getDocenteReemplazo().getId())
                .docenteReemplazoNombre(r.getDocenteReemplazo().getUsuario().getPersona().getNombres())
                .cronogramaDiarioId(r.getCronogramaDiario().getId())
                .motivo(r.getMotivo())
                .tipoReemplazoId(r.getTipoReemplazo().getId())
                .tipoReemplazoNombre(r.getTipoReemplazo().getNombreTipoReemplazo())
                .estadoReemplazo(r.getEstado())
                .fechaRegistro(r.getFechaRegistro())
                .build();
    }

}
