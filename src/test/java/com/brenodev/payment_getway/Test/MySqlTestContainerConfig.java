package com.brenodev.payment_getway.Test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MySqlTestContainerConfig {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {

        return new MySQLContainer<>("mysql:8.4")
                .withDatabaseName("payment_gateway_test")
                .withUsername("test")
                .withPassword("test");
    }
}