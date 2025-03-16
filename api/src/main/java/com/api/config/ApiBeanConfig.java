package com.api.config;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
@ComponentScan(basePackages = {"com.api", "com.testproj.db", "com.testproj.db.schema"})
public class ApiBeanConfig {
    @Bean
    public BeanMapper beanMapper(){
        return new BeanMapperBuilder().withoutDefaultConverters().build();
    }
}