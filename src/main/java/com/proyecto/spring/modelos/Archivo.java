package com.proyecto.spring.modelos;

import java.time.LocalDateTime;
import java.util.List;

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
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida = LocalDateTime.now();

    @OneToMany(mappedBy = "archivo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    private String asignatura;   //materia a la que pertenece el archivo
    private String nivelEstudio; //nivel educativo (ESO, Bachillerato, Universidad, etc.)
}
