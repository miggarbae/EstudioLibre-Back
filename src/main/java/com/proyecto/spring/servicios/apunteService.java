package com.proyecto.spring.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.spring.modelo.apunte;
import com.proyecto.spring.repositorios.apunteRepository;

@Service
public class apunteService {
    @Autowired
    private apunteRepository apunteRepositorio;

    public List<apunte> listarApuntes() {
        return apunteRepositorio.findAll();
    }

    public Optional<apunte> obtenerApuntePorId(Long id) {
        return apunteRepositorio.findById(id);
    }

    public apunte guardarApunte(apunte apunte) {
        return apunteRepositorio.save(apunte);
    }

    public void eliminarApunte(Long id) {
        apunteRepositorio.deleteById(id);
    }
}
