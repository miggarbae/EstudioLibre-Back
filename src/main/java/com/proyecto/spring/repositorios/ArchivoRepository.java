package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    List<Archivo> findByUsuarioId(Long usuarioId);
    
    

    // Búsqueda dinámica por nombre, asignatura y nivel de estudio
    @Query("""
        SELECT a FROM Archivo a
        WHERE (:termino IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(a.asignatura) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(a.nivelEstudio) LIKE LOWER(CONCAT('%', :termino, '%'))
            OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')))
    """)
    List<Archivo> buscarArchivos(@Param("termino") String termino);

}