package com.example.client;

import com.example.client.config.properties.DatabaseH2Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseH2Properties.class)
public class AzureWebAppClientAdApplication {

    static void main(String[] args) {
        SpringApplication.run(AzureWebAppClientAdApplication.class, args);
    }

}
