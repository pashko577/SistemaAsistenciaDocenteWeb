package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
 @NoArgsConstructor
  @Getter 
  @Setter
public class DocenteRegistroActividadesAcademicasResponseDTO extends DocenteRegistroAsistenciaResponseDTO {
    private Boolean publiWSS;
    private Boolean publiWFS;
    private Boolean agendaE;
    private Boolean usoTerno;

}
