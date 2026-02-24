package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;
import com.example.actividades_app.repository.CargoAdministrativoRepository;
import com.example.actividades_app.service.CargoAdministrativoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CargoAdministrativoImpl implements CargoAdministrativoService{
    private final CargoAdministrativoRepository cargoAdministrativoRepository;

    @Override
    public CargoAdministrativo crearCargoAdministrativo(CargoAdministrativoRequestDTO dto){

        if (cargoAdministrativoRepository.existsByNombreCargo(dto.getNombreCargo())) {
            throw new RuntimeException("La sede ya existe");
        }

        CargoAdministrativo cargoAdministrativo = CargoAdministrativo.builder()
                .nombreCargo(dto.getNombreCargo())
                .build();
        
        return cargoAdministrativoRepository.save(cargoAdministrativo);
    }

    @Override
    public CargoAdministrativo actualizarAdministrativo(Long cargoAdministrativoID, String nombreCargo){
        CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository.findById(cargoAdministrativoID)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
        
        cargoAdministrativo.setNombreCargo(nombreCargo);

        return cargoAdministrativoRepository.save(cargoAdministrativo);
    }

    @Override
    public void eliminarCargoAdministrativo(Long cargoAdministrativoID){
        CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository.findById(cargoAdministrativoID)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));
        
        cargoAdministrativoRepository.delete(cargoAdministrativo);
    }

    @Override
    public List<CargoAdministrativo> listarTodosCargos(){
        return cargoAdministrativoRepository.findAll();
    }
}
