package com.example.client.controller;

import com.example.client.service.DemoAppConfigurationService;
import com.example.client.service.KeyVaultSecretService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DemoController {

    private final KeyVaultSecretService keyVaultSecretService;
    private final DemoAppConfigurationService  demoAppConfigurationService;

    public DemoController(KeyVaultSecretService keyVaultSecretService,
                          DemoAppConfigurationService demoAppConfigurationService) {
        this.keyVaultSecretService = keyVaultSecretService;
        this.demoAppConfigurationService = demoAppConfigurationService;
    }

    @Value("${azure.app.key_vault.secret_name}")
    private String secretName;

    @GetMapping(value = "/secret", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getSecret() {
        String secret = keyVaultSecretService.getSecret(secretName);
        return ResponseEntity.ok(secret);
    }

    @GetMapping("/app/configs")
    public ResponseEntity<Map<String, String>> applicationConfigs() {
        Map<String, String> appConfigs = demoAppConfigurationService.getAppConfiguraitons();
        return ResponseEntity.ok(appConfigs);
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTestValue() {
        return ResponseEntity.ok("test");
    }
}
