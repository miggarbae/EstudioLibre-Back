package com.proyecto.spring.modelos;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String motivo;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    private Archivo archivo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {   // guardar la fecha automáticamente al crear un reporte
        this.fecha = LocalDate.now();
    }
}