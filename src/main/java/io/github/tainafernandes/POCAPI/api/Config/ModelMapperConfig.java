package io.github.tainafernandes.POCAPI.api.Config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
