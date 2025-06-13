package com.example.tinyhouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.tinyhouse.entities")
@EnableJpaRepositories("com.example.tinyhouse")
@ComponentScan(basePackages = {"com.example.tinyhouse"})
public class TinyHouseBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinyHouseBackendApplication.class, args);
	}
}
