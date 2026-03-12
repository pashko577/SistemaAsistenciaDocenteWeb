package com.example.actividades_app.model.dto.ModuloCurso;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 100)
    private String nombreCurso;
}
