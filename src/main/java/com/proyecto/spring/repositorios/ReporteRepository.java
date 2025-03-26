package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> { }
