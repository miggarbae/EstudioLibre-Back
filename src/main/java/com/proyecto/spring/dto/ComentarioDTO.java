package com.proyecto.spring.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComentarioDTO {
    private Long id;
    private String usuario;
    private String imagenUsuario; // nueva propiedad
    private Long archivoId;
    private String texto;
    private Integer valoracion;
    private LocalDateTime fechaCreacion;

    public ComentarioDTO(Long id, String usuario, String imagenUsuario, Long archivoId, String texto, int valoracion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuario = usuario;
        this.imagenUsuario = imagenUsuario;
        this.archivoId = archivoId;
        this.texto = texto;
        this.valoracion = valoracion;
        this.fechaCreacion = fechaCreacion;
    }

    public void setImagenUsuario(String imagenUsuario) { this.imagenUsuario = imagenUsuario; }
}