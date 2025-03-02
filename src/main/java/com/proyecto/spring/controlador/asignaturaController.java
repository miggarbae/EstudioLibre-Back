package com.proyecto.spring.controlador;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/asignaturas")
public class asignaturaController {
    @Autowired
    private asignaturaService asignaturaServicio;

    @GetMapping
    public List<asignatura> listarAsignaturas() {
        return asignaturaServicio.listarAsignaturas();
    }

    @GetMapping("/{id}")
    public Optional<asignatura> obtenerAsignatura(@PathVariable Long id) {
        return asignaturaServicio.obtenerAsignaturaPorId(id);
    }

    @PostMapping
    public asignatura crearAsignatura(@RequestBody asignatura asignatura) {
        return asignaturaServicio.guardarAsignatura(asignatura);
    }

    @DeleteMapping("/{id}")
    public void eliminarAsignatura(@PathVariable Long id) {
        asignaturaServicio.eliminarAsignatura(id);
    }
}
