package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
import com.example.actividades_app.repository.EspecialidadDocenteRepository;
import com.example.actividades_app.service.EspecialidadDocenteService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadDocenteServiceImpl implements EspecialidadDocenteService{

    private final EspecialidadDocenteRepository especialidadDocenteRepository;

    @Override
    public EspecialidadDocente crearEspecialidadDocente(EspecialidadDocenteRequestDTO dto){
        if (especialidadDocenteRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("La especialidad ya existe");            
        }

        EspecialidadDocente especialidadDocente = EspecialidadDocente.builder()
            .nombreEspecialidad(dto.getNombreEspecialidad())
            .build();
        
        return especialidadDocenteRepository.save(especialidadDocente);
    }

    @Override
    public EspecialidadDocente actualizarEspecialidadDocente(Long especialidadDocenteID, String nombreEspecialidad){
        EspecialidadDocente especialidadDocente = especialidadDocenteRepository.findById(especialidadDocenteID)
                .orElseThrow(()-> new RuntimeException("Especialidad no encontrada"));
        especialidadDocente.setNombreEspecialidad(nombreEspecialidad);

        return especialidadDocenteRepository.save(especialidadDocente);
    }

    @Override
    public void eliminarEspecilidadDocente(Long especialidadDocenteID){
        EspecialidadDocente especialidadDocente = especialidadDocenteRepository.findById(especialidadDocenteID)
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        
        especialidadDocenteRepository.delete(especialidadDocente);
    }

    @Override
    public EspecialidadDocente obtenerPorId(Long especialidadDocenteID){
        return especialidadDocenteRepository.findById(especialidadDocenteID)
                .orElseThrow(()-> new RuntimeException("Especialidad no encontrada")); 
    }

    @Override
    public List<EspecialidadDocente> listarEspecialidadDocente(){
        return especialidadDocenteRepository.findAll();
    }
}
