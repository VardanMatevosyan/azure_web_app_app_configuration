package com.example.client.service.impl;

import com.example.client.config.properties.DatabaseH2Properties;
import com.example.client.service.DemoAppConfigurationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DemoAppConfigurationServiceImpl implements DemoAppConfigurationService {

    private final DatabaseH2Properties databaseH2Properties;

    public DemoAppConfigurationServiceImpl(DatabaseH2Properties databaseH2Properties) {
        this.databaseH2Properties = databaseH2Properties;
    }

    @Override
    public Map<String, String> getAppConfiguraitons() {
        System.out.println("host: " + databaseH2Properties.getHost());
        return Map.of(
                "db.host", databaseH2Properties.getHost(),
                "db.port", databaseH2Properties.getPort(),
                "db.username", databaseH2Properties.getUsername(),
                "db.password", databaseH2Properties.getPassword()
        );
    }

}
