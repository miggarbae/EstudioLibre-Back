package com.proyecto.spring.modelo;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data

public class apunte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contenido;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private usuario usuario;

    @ManyToOne
    @JoinColumn(name = "asignatura_id")
    private asignatura asignatura;

    @OneToMany(mappedBy = "apunte")
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "apunte")
    private List<valoracion> valoraciones;
}
