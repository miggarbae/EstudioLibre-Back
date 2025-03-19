package com.proyecto.spring.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String texto;  // Contenido del comentario

    @Column(nullable = false)
    private int valoracion;  // Puntuación (1 a 5)

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;  // Fecha del comentario
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;  // Quién hizo el comentario

    @ManyToOne
    @JoinColumn(name = "archivo_id", nullable = false)
    @JsonIgnore
    private Archivo archivo;  // A qué archivo se refiere el comentario
}

