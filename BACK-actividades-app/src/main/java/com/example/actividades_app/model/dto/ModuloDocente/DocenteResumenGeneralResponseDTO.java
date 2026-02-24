package com.example.actividades_app.model.dto.ModuloDocente;

import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocenteResumenGeneralResponseDTO extends ResumenGeneralResponseDTO {
    private Double horasDictadas;
    private Integer TipoAsistencia;
}


