package net.testproj.api.config;


import org.flywaydb.core.Flyway;
import org.jooq.impl.DataSourceConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Bean(name = "authDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource() {return new DriverManagerDataSource();}

    @Bean(name = "publicDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.public")
    public DataSource publicDataSource() {return new DriverManagerDataSource();}

    @Bean
    public Flyway flywayAuth(@Qualifier("authDataSource") DataSource authDataSource) {
        Flyway flyway = Flyway.configure().dataSource(authDataSource)
                .locations("classpath:db/migration/auth_migration")
                .schemas("auth").load();
        return flyway;
    }

    @Bean
    public Flyway flywayPublic(@Qualifier("publicDataSource") DataSource publicDataSource) {
        Flyway flyway =  Flyway.configure().dataSource(publicDataSource)
                .locations("classpath:db/migration/public_migration")
                .schemas("public").load();
        return flyway;
    }

    @Bean
    public DataSourceConnectionProvider publicDataSourceConnectionProvider(@Qualifier("publicDataSource") DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public DataSourceConnectionProvider authDataSourceConnectionProvider(@Qualifier("authDataSource") DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }
}