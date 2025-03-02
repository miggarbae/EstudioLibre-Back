package com.proyecto.spring.controlador;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class comentarioController {
    @Autowired
    private comentarioService comentarioServicio;

    @GetMapping
    public List<Comentario> listarComentarios() {
        return comentarioServicio.listarComentarios();
    }

    @GetMapping("/{id}")
    public Optional<Comentario> obtenerComentario(@PathVariable Long id) {
        return comentarioServicio.obtenerComentarioPorId(id);
    }

    @PostMapping
    public Comentario crearComentario(@RequestBody Comentario comentario) {
        return comentarioServicio.guardarComentario(comentario);
    }

    @DeleteMapping("/{id}")
    public void eliminarComentario(@PathVariable Long id) {
        comentarioServicio.eliminarComentario(id);
    }
}
