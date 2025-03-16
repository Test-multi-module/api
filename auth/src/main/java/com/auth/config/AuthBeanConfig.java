package com.auth.config;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.auth", "com.testproj.db"})
public class AuthBeanConfig {
    @Bean
    public BeanMapper beanMapper(){
        return new BeanMapperBuilder().withoutDefaultConverters().build();
    }
}
