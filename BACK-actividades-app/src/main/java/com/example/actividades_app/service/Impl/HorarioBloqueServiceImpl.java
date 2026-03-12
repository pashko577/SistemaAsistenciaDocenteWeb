package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.HorarioBloque;
import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueRequestDTO;
import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueResponseDTO;
import com.example.actividades_app.repository.HorarioBloqueRepository;
import com.example.actividades_app.service.HorarioBloqueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HorarioBloqueServiceImpl implements HorarioBloqueService {

    private final HorarioBloqueRepository horarioBloqueRepository;

    @Override
    public HorarioBloqueResponseDTO crear(HorarioBloqueRequestDTO dto) {

        if (horarioBloqueRepository.existsByOrdenBloque(dto.getOrdenBloque())) {
            throw new RuntimeException("El orden del bloque ya existe");
        }

        HorarioBloque bloque = HorarioBloque.builder()
                .horaInicio(dto.getHoraInicio())
                .horaFin(dto.getHoraFin())
                .ordenBloque(dto.getOrdenBloque())
                .build();

        horarioBloqueRepository.save(bloque);

        return mapToResponse(bloque);
    }

    @Override
    public List<HorarioBloqueResponseDTO> listar() {
        return horarioBloqueRepository.findAllByOrderByOrdenBloqueAsc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public HorarioBloqueResponseDTO obtenerPorId(Long id) {

        HorarioBloque bloque = horarioBloqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

        return mapToResponse(bloque);
    }

    @Override
    public HorarioBloqueResponseDTO actualizar(Long id, HorarioBloqueRequestDTO dto) {

        HorarioBloque bloque = horarioBloqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bloque no encontrado"));

        bloque.setHoraInicio(dto.getHoraInicio());
        bloque.setHoraFin(dto.getHoraFin());
        bloque.setOrdenBloque(dto.getOrdenBloque());

        horarioBloqueRepository.save(bloque);

        return mapToResponse(bloque);
    }

    @Override
    public void eliminar(Long id) {
        horarioBloqueRepository.deleteById(id);
    }

    private HorarioBloqueResponseDTO mapToResponse(HorarioBloque bloque) {

        return HorarioBloqueResponseDTO.builder()
                .id(bloque.getId())
                .horaInicio(bloque.getHoraInicio())
                .horaFin(bloque.getHoraFin())
                .ordenBloque(bloque.getOrdenBloque())
                .build();
    }

}