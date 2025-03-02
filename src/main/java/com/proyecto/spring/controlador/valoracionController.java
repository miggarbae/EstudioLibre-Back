package com.proyecto.spring.controlador;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/valoraciones")
public class valoracionController {
    @Autowired
    private valoracionService valoracionServicio;

    @GetMapping
    public List<valoracion> listarValoraciones() {
        return valoracionServicio.listarValoraciones();
    }

    @GetMapping("/{id}")
    public Optional<valoracion> obtenerValoracion(@PathVariable Long id) {
        return valoracionServicio.obtenerValoracionPorId(id);
    }

    @PostMapping
    public valoracion crearValoracion(@RequestBody valoracion valoracion) {
        return valoracionServicio.guardarValoracion(valoracion);
    }

    @DeleteMapping("/{id}")
    public void eliminarValoracion(@PathVariable Long id) {
        valoracionServicio.eliminarValoracion(id);
    }
}
