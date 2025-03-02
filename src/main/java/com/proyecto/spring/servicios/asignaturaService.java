package com.proyecto.spring.servicios;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class asignaturaService {
    @Autowired
    private asignaturaRepository asignaturaRepositorio;

    public List<asignatura> listarAsignaturas() {
        return asignaturaRepositorio.findAll();
    }

    public Optional<asignatura> obtenerAsignaturaPorId(Long id) {
        return asignaturaRepositorio.findById(id);
    }

    public asignatura guardarAsignatura(asignatura asignatura) {
        return asignaturaRepositorio.save(asignatura);
    }

    public void eliminarAsignatura(Long id) {
        asignaturaRepositorio.deleteById(id);
    }
}
