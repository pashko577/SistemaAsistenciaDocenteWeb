package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoResponseDTO;
import com.example.actividades_app.repository.CargoAdministrativoRepository;
import com.example.actividades_app.service.CargoAdministrativoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CargoAdministrativoImpl implements CargoAdministrativoService {

    private final CargoAdministrativoRepository cargoAdministrativoRepository;

    @Override
    public CargoAdministrativoResponseDTO crearCargoAdministrativo(CargoAdministrativoRequestDTO dto) {

        if (cargoAdministrativoRepository.existsByNombreCargo(dto.getNombreCargo())) {
            throw new RuntimeException("El cargo administrativo ya existe");
        }

        CargoAdministrativo cargoAdministrativo = CargoAdministrativo.builder()
                .nombreCargo(dto.getNombreCargo())
                .build();

        CargoAdministrativo guardado = cargoAdministrativoRepository.save(cargoAdministrativo);

        return mapToResponse(guardado);
    }

    @Override
    public CargoAdministrativoResponseDTO actualizarAdministrativo(Long cargoAdministrativoID, String nombreCargo) {
        CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository.findById(cargoAdministrativoID)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        if (cargoAdministrativoRepository.existsByNombreCargo(nombreCargo)
                && !cargoAdministrativo.getNombreCargo().equalsIgnoreCase(nombreCargo)) {
            throw new RuntimeException("Ya existe un cargo con ese nombre");
        }

        cargoAdministrativo.setNombreCargo(nombreCargo);

        CargoAdministrativo actualizado = cargoAdministrativoRepository.save(cargoAdministrativo);

        return mapToResponse(actualizado);
    }

    @Override
    public void eliminarCargoAdministrativo(Long cargoAdministrativoID) {
        CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository.findById(cargoAdministrativoID)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        cargoAdministrativoRepository.delete(cargoAdministrativo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CargoAdministrativoResponseDTO> listarTodosCargos() {
        return cargoAdministrativoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CargoAdministrativoResponseDTO mapToResponse(CargoAdministrativo cargoAdministrativo) {
        return CargoAdministrativoResponseDTO.builder()
                .id(cargoAdministrativo.getId())
                .nombreCargo(cargoAdministrativo.getNombreCargo())
                .build();
    }
}