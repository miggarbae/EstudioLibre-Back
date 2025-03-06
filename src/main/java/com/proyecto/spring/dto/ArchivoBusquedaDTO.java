package com.proyecto.spring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArchivoBusquedaDTO {
    private String nombre;
    private String asignatura;
    private String nivelEstudio;
    private String ordenarPor;  // fecha o valoracion
}
