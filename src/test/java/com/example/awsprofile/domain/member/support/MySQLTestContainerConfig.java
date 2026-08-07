package com.example.awsprofile.domain.member.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.mysql.MySQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class MySQLTestContainerConfig {
    //MySQL 컨테이너를 테스트 컨텍스트에 추가
    @Bean
    @ServiceConnection
    public MySQLContainer mysqlContainer() {
        return new MySQLContainer("mysql:8.4")
                .withDatabaseName("test")
                .withUsername("root")
                .withPassword("root");
    }
}
