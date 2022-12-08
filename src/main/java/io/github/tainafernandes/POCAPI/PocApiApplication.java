package io.github.tainafernandes.POCAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PocApiApplication {
	@Bean
	public ModelMapper mapper(){
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(PocApiApplication.class, args);
	}

}
