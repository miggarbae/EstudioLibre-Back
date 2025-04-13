package com.proyecto.spring.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USER;

    private String rutaImagenPerfil;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Archivo> archivos;

    // tranforma la ruta real en dusco en una ruta accesible via http
    // por ejemplo: /perfiles/imagen.jpg
    public String getRutaImagenPerfil() {
        if (rutaImagenPerfil != null && !rutaImagenPerfil.isEmpty()) {
            String nombreArchivo = new java.io.File(rutaImagenPerfil).getName();
            return "/perfiles/" + nombreArchivo;
        }
        return null;
    }
}