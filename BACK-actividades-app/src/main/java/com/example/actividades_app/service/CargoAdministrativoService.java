package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoResponseDTO;

public interface CargoAdministrativoService {
    CargoAdministrativoResponseDTO crearCargoAdministrativo(CargoAdministrativoRequestDTO dto);

    CargoAdministrativoResponseDTO actualizarAdministrativo(Long cargoAdministrativoID, String nombreCargo);

    void eliminarCargoAdministrativo(Long cargoAdministrativoID);

    List<CargoAdministrativoResponseDTO> listarTodosCargos();
}
