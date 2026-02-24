package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;

public interface CargoAdministrativoService {
    CargoAdministrativo crearCargoAdministrativo(CargoAdministrativoRequestDTO dto);

    CargoAdministrativo actualizarAdministrativo(Long cargoAdministrativoID, String nombreCargo);

    void eliminarCargoAdministrativo(Long cargoAdministrativoID);

    List<CargoAdministrativo> listarTodosCargos();
}
