package com.example.actividades_app.model.dto.ModuloHorario;





import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HorarioDocenteResponseDTO {

    private Long cronogramaId;
    private String fecha;
    private String horaInicioClase;
    private String horaFinClase;
    private String tema;
    private String curso;
    private String grado;
    private String seccion;
    private String nivel;
    private String aula;
    private String docenteNombre; // nombres + apellidos desde Persona
}


