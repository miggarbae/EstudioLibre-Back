package com.proyecto.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.spring.modelo.apuntes;

public interface apuntesRepository extends JpaRepository<apuntes, Long> {

}

