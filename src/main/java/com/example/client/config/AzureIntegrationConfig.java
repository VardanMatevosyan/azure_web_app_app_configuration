package com.example.client.config;

import com.azure.identity.ManagedIdentityCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local")
public class AzureIntegrationConfig {

    @Value("${azure.app.key_vault.url}")
    private String keyVaultUrl;


    @Bean
    public SecretClient secretClient() {
        return new SecretClientBuilder()
                .vaultUrl(keyVaultUrl)
                .credential(new ManagedIdentityCredentialBuilder().build())
                .buildClient();
    }

}
