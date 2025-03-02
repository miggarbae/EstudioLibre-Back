package com.proyecto.spring.servicios;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class valoracionService {
    @Autowired
    private valoracionRepository valoracionRepositorio;

    public List<valoracion> listarValoraciones() {
        return valoracionRepositorio.findAll();
    }

    public Optional<valoracion> obtenerValoracionPorId(Long id) {
        return valoracionRepositorio.findById(id);
    }

    public valoracion guardarValoracion(valoracion valoracion) {
        return valoracionRepositorio.save(valoracion);
    }

    public void eliminarValoracion(Long id) {
        valoracionRepositorio.deleteById(id);
    }
}
