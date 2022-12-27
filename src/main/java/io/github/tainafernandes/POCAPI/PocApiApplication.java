package io.github.tainafernandes.POCAPI;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@EnableCaching
public class PocApiApplication
{

	public static void main(String[] args) {
		SpringApplication.run(PocApiApplication.class, args);
	}
	@Configuration
	public class ModelMapperConfig {
		@Bean
		public ModelMapper mapper(){
			return new ModelMapper();
		}
	}

}
