package com.proyecto.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.spring.modelo.apunte;

public interface apunteRepository extends JpaRepository<apunte, Long> {

}

