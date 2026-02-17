package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_reemplazo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoReemplazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_ReemplazoID")
    private Long id;

    @Column(name = "nombreTipoReemplazo", nullable = false, length = 100)
    private String nombreTipoReemplazo;

}
