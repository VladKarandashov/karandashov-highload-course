package com.karandashov.monolith.configuration.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("database")
public class DatabaseProperties {

    private String driver;
    private String url;
    private String username;
    private String password;
    private String schema;

    private ConnectionPoolProperties connectionPool;

    @Data
    public static class ConnectionPoolProperties {

        private Integer maximumPoolSize;   // Максимальное количество соединений в пуле
        private Integer minimumIdle;       // Минимальное количество простаивающих соединений
        private Integer idleTimeout;       // Время простоя соединения (в миллисекундах)
        private Integer maxLifetime;       // Максимальное время жизни соединения
        private Integer connectionTimeout; // Timeout ожидания соединения
    }
}
