package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long>{
    List<Comentario> findByArchivoId(Long archivoId);
    Optional<Comentario> findByArchivoIdAndUsuarioId(Long archivoId, Long usuarioId);
}