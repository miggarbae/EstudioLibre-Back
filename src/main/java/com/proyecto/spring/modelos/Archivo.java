package com.proyecto.spring.modelos;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private String asignatura;   //materia a la que pertenece el archivo
    private String nivelEstudio; //nivel educativo (ESO, Bachillerato, Universidad, etc.)
    private String descripcion; //breve descripción del archivo

    @ManyToOne
    @JsonIgnore // Evita la recursividad infinita
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida = LocalDateTime.now();

    @OneToMany(mappedBy = "archivo", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comentario> comentarios;
}
