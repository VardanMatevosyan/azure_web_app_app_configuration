package com.example.client.service.impl;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.example.client.service.KeyVaultSecretService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KeyVaultSecretServiceImpl implements KeyVaultSecretService {

    private static final Logger log = LoggerFactory.getLogger(KeyVaultSecretServiceImpl.class);

    public KeyVaultSecretServiceImpl(SecretClient secretClient) {
        this.secretClient = secretClient;
    }

    private final SecretClient secretClient;

    @Override
    public String getSecret(String secretName) {
        try {
            KeyVaultSecret secret = secretClient.getSecret(secretName);
            return secret.getValue();
        } catch (Exception e) {
            log.error("get secret failed {}", e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
