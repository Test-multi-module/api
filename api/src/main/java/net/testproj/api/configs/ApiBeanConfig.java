package net.testproj.api.configs;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiBeanConfig {
    @Bean
    public BeanMapper beanMapper(){
        return new BeanMapperBuilder().withoutDefaultConverters().build();
    }
}