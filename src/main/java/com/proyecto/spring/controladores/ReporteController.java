package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.Reporte;
import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.repositorios.UsuarioRepository;
import com.proyecto.spring.servicios.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "http://localhost:4200")
public class ReporteController {

    private final ReporteService reporteService;
    private final UsuarioRepository usuarioRepository;

    public ReporteController(ReporteService reporteService, UsuarioRepository usuarioRepository) {
        this.reporteService = reporteService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<?> crearReporte(@RequestBody Reporte reporte, Principal principal) {
        Usuario usuario = usuarioRepository.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        reporte.setUsuario(usuario);
        reporteService.guardar(reporte);
        return ResponseEntity.ok("Reporte guardado correctamente");
    }

    @GetMapping
    public ResponseEntity<List<Reporte>> obtenerReportes() {
        return ResponseEntity.ok(reporteService.obtenerTodos());
    }
}