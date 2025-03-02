package com.proyecto.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EstudioLibreApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudioLibreApplication.class, args);
	}

}
