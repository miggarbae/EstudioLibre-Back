package com.proyecto.spring.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "archivos")
public class Archivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;

    @Lob // Almacena archivos binarios en la BD
    private byte[] datos;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
