package com.proyecto.spring.dto;

import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long id;
    private String usuario;
    private Long archivoId;
    private String texto;
    private int valoracion;
    private LocalDateTime fechaCreacion;

    public ComentarioDTO(Long id, String usuario, Long archivoId, String texto, int valoracion, LocalDateTime fechaCreacion) {
        this.id = id;
        this.usuario = usuario;
        this.archivoId = archivoId;
        this.texto = texto;
        this.valoracion = valoracion;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public Long getArchivoId() { return archivoId; }
    public String getTexto() { return texto; }
    public int getValoracion() { return valoracion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
