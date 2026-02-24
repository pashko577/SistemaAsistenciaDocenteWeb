package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocenteRegistroAcademiaPreUniResponseDTO extends DocenteRegistroAsistenciaResponseDTO {

    private Boolean presentoMaterial;
    private Boolean usoTerno;
    private Boolean publicacionWSS;

}
