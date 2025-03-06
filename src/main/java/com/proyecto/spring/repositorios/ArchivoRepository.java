package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    List<Archivo> findByUsuarioId(Long usuarioId);
}
