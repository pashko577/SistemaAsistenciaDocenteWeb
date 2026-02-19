package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.CargoAdministrativo;

@Repository
public interface CargoAdministrativoRepository extends JpaRepository<CargoAdministrativo, Long> {

        Optional<CargoAdministrativo> findByNombreCargo(String nombreCargo);

}
