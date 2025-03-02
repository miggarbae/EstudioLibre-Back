package com.proyecto.spring.servicios;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service

public class comentarioService {
    @Autowired
    private comentarioRepository comentarioRepositorio;

    public List<Comentario> listarComentarios() {
        return comentarioRepositorio.findAll();
    }

    public Optional<Comentario> obtenerComentarioPorId(Long id) {
        return comentarioRepositorio.findById(id);
    }

    public Comentario guardarComentario(Comentario comentario) {
        return comentarioRepositorio.save(comentario);
    }

    public void eliminarComentario(Long id) {
        comentarioRepositorio.deleteById(id);
    }
}
