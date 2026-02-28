package com.karandashov.monolith.configuration;

import com.karandashov.monolith.configuration.property.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration {

    private final DatabaseProperties databaseProperties;

    @Bean
    public DataSource connectionPoolDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseProperties.getUrl());
        config.setUsername(databaseProperties.getUsername());
        config.setPassword(databaseProperties.getPassword());
        config.setDriverClassName(databaseProperties.getDriver());
        config.setSchema(databaseProperties.getSchema());

        var connectionPoolProperties = databaseProperties.getConnectionPool();
        config.setMaximumPoolSize(connectionPoolProperties.getMaximumPoolSize());
        config.setMinimumIdle(connectionPoolProperties.getMinimumIdle());
        config.setIdleTimeout(connectionPoolProperties.getIdleTimeout());
        config.setMaxLifetime(connectionPoolProperties.getMaxLifetime());
        config.setConnectionTimeout(connectionPoolProperties.getConnectionTimeout());

        return new HikariDataSource(config);
    }
}
