package com.testproj.api.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "authDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource() {return new DriverManagerDataSource();}

    @Bean(name = "publicDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.public")
    public DataSource publicDataSource() {return new DriverManagerDataSource();}
}
