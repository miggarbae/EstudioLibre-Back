package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByArchivoId(Long archivoId);
}
