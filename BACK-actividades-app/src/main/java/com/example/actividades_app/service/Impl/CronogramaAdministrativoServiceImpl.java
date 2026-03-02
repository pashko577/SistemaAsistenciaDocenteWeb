package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.CronogramaAdministrativoService;

@Service
@RequiredArgsConstructor
public class CronogramaAdministrativoServiceImpl implements CronogramaAdministrativoService {

    private final CronogramaAdministrativoRepository repository;
    private final AdministrativoRepository administrativoRepository;

    // =======================
    // CREAR
    // =======================
    @Override
    public CronogramaAdministrativoResponseDTO crear(CronogramaAdministrativoRequestDTO dto) {

        Administrativo admin = administrativoRepository.findById(dto.getAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

        // Validar duplicado
        if (repository.existsByAdministrativoIdAndFecha(dto.getAdministrativoId(), dto.getFecha())) {
            throw new RuntimeException("Ya existe cronograma para esa fecha");
        }

        // Validar horas
        if (dto.getHoraEntrada().isAfter(dto.getHoraSalida())) {
            throw new RuntimeException("Hora de entrada no puede ser mayor a hora de salida");
        }

        CronogramaAdministrativo cronograma = CronogramaAdministrativo.builder()
                .horaEntrada(dto.getHoraEntrada())
                .horaSalida(dto.getHoraSalida())
                .fecha(dto.getFecha())
                .administrativo(admin)
                .build();

        CronogramaAdministrativo saved = repository.save(cronograma);

        return mapToResponseDTO(saved);
    }

    // =======================
    // ACTUALIZAR
    // =======================
    @Override
    public CronogramaAdministrativoResponseDTO actualizar(Long id, CronogramaAdministrativoRequestDTO dto) {

        CronogramaAdministrativo cronograma = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));

        if (dto.getHoraEntrada().isAfter(dto.getHoraSalida())) {
            throw new RuntimeException("Hora de entrada no puede ser mayor a hora de salida");
        }

        cronograma.setHoraEntrada(dto.getHoraEntrada());
        cronograma.setHoraSalida(dto.getHoraSalida());
        cronograma.setFecha(dto.getFecha());

        // Si se desea actualizar administrativo:
        if (!cronograma.getAdministrativo().getId().equals(dto.getAdministrativoId())) {
            Administrativo admin = administrativoRepository.findById(dto.getAdministrativoId())
                    .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));
            cronograma.setAdministrativo(admin);
        }

        return mapToResponseDTO(repository.save(cronograma));
    }

    // =======================
    // ELIMINAR
    // =======================
    @Override
    public void eliminar(Long id) {
        CronogramaAdministrativo cronograma = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));
        repository.delete(cronograma);
    }

    // =======================
    // BUSCAR POR ID
    // =======================
    @Override
    public CronogramaAdministrativoResponseDTO buscarPorId(Long id) {
        CronogramaAdministrativo cronograma = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));
        return mapToResponseDTO(cronograma);
    }

    // =======================
    // LISTAR POR ADMINISTRATIVO
    // =======================
    @Override
    public List<CronogramaAdministrativoResponseDTO> listarPorAdministrativo(Long administrativoId) {
        return repository.findByAdministrativoId(administrativoId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // =======================
    // LISTAR POR FECHA
    // =======================
    @Override
    public List<CronogramaAdministrativoResponseDTO> listarPorFecha(LocalDate fecha) {
        return repository.findByFecha(fecha)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

        @Override
public CronogramaAdministrativoResponseDTO buscarPorAdministrativoYFecha(Long administrativoId, LocalDate fecha) {

    CronogramaAdministrativo cronograma = repository
            .findByAdministrativoIdAndFecha(administrativoId, fecha)
            .orElseThrow(() -> new RuntimeException("No existe cronograma para este administrativo en la fecha indicada"));

    return mapToResponseDTO(cronograma);
}
    // =======================
    // MAPPER
    // =======================
    private CronogramaAdministrativoResponseDTO mapToResponseDTO(CronogramaAdministrativo cronograma) {
        CronogramaAdministrativoResponseDTO dto = new CronogramaAdministrativoResponseDTO();
        dto.setId(cronograma.getId());
        dto.setHoraEntrada(cronograma.getHoraEntrada());
        dto.setHoraSalida(cronograma.getHoraSalida());
        dto.setFecha(cronograma.getFecha());
        dto.setAdministrativoId(cronograma.getAdministrativo().getId());
        return dto;
    }


}