package com.proyecto.spring.servicios;

import com.proyecto.spring.modelos.Reporte;
import com.proyecto.spring.repositorios.ReporteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {
    private final ReporteRepository reporteRepository;

    public ReporteService(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    public Reporte guardar(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public List<Reporte> obtenerTodos() {
        return reporteRepository.findAll();
    }
}
