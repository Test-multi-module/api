package net.testproj.api.config;

import io.beanmapper.BeanMapper;
import io.beanmapper.config.BeanMapperBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ApiBeanConfig {
    @Bean//decided to set here as converter probably will be needed and added soon
    public BeanMapper beanMapper(){
        return new BeanMapperBuilder().withoutDefaultConverters().build();
    }
}