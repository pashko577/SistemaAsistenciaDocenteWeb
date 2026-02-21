package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.dto.ModuloUsuario.SedeRequestDTO;
import com.example.actividades_app.repository.SedeRepository;
import com.example.actividades_app.service.SedeService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SedeServiceImpl implements SedeService {

    private final SedeRepository sedeRepository;

    @Override
    public Sede crearSede(SedeRequestDTO dto) {

        if (sedeRepository.existsByNombreSede(dto.getNombreSede())) {
            throw new RuntimeException("La sede ya existe");
        }

        Sede sede = Sede.builder()
                .nombreSede(dto.getNombreSede())
                .build();

        return sedeRepository.save(sede);
    }

    @Override
    public Sede actualizarSede(Long sedeId, String nombre) {

        Sede sede = sedeRepository.findById(sedeId)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        sede.setNombreSede(nombre);

        return sedeRepository.save(sede);
    }

    @Override
    public void eliminarSede(Long sedeId) {

        Sede sede = sedeRepository.findById(sedeId)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

        sedeRepository.delete(sede);
    }

    @Override
    @Transactional(readOnly = true)
    public Sede obtenerPorId(Long sedeId) {

        return sedeRepository.findById(sedeId)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sede> listarTodas() {

        return sedeRepository.findAll();
    }
}
