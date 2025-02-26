package com.proyecto.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.spring.modelo.Comentario;

public interface comentarioRepository extends JpaRepository<Comentario, Long> {

}
