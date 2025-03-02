package com.proyecto.spring.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.spring.modelo.apunte;
import com.proyecto.spring.servicios.apunteService;

@RestController
@RequestMapping("/apuntes")
@CrossOrigin(origins = "http://localhost:4200")
public class apunteController {
        @Autowired
    private apunteService apunteServicio;

    @GetMapping
    public List<apunte> listarApuntes() {
        return apunteServicio.listarApuntes();
    }

    @GetMapping("/{id}")
    public Optional<apunte> obtenerApunte(@PathVariable Long id) {
        return apunteServicio.obtenerApuntePorId(id);
    }

    @PostMapping
    public apunte crearApunte(@RequestBody apunte apunte) {
        return apunteServicio.guardarApunte(apunte);
    }

    @DeleteMapping("/{id}")
    public void eliminarApunte(@PathVariable Long id) {
        apunteServicio.eliminarApunte(id);
    }
}
