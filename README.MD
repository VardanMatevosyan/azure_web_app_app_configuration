# Testing App Services Web App with Azure App Configuration Key Value and Key Vault Reference

## azure_web_app_app_configuraiton
Azure Cloud testing with application configuration and secrets auto update using triggers props and refresh count app configuration.

# Resources involved
1. App Service Plans
2. App Services
3. Resource Group
4. Container Registries
5. App Configuration
6. Key Vault
7. Managed Identities
8. Microsoft Entra ID

# Tools
1. Azure web UI
2. Azure CLI
3. Spring Boot
4. Springframework Azure Cloud Starters

# Steps

## Azure setup

1. Create a Service Plan. The Resource Group will be created automatically. For all related resource choose this group.
2. Create Container Registries.
3. Deploy the application using steps from the Deploy from local section.
4. Create a Web App associated with created Service Plan from step 1. Choose the Container image source as Azure Container Registry.
5. Create a App Configuration
6. Add App Configuration Data Owner Administrator role assigment in App Configuration's Access Control (IAM) to yourself to create the configs.
7. Create a Key Vault. Add Key Vault Administrator role assigment in Key Vault Access Control (IAM) to yourself to create the secrets.
8. Create a Access Control (IAM) Managed Identity in the Key Vault for previously created App configuration resource. The role assigment name is
   Key Vault Secrets User. This is needed for App Configuration to access and read the Key Vault resource data.
9. Create a 3 Key Value pair and Key Vault Reference from created App Configuration from step 3 with label that matches the environment (dev, qa, prod).
   * The Key Value name should start with /applicaiton/. Spring Boot Azure Starter configuration dependency 
        will understand this and in the applicaiton.yml there is a configuration that can trim this part.
   * The second part of the Key Value name should start with the property name separated by dot "." like db.host.
   * Create 3 Key Value pair with these names: /application/db.h2.host, /application/db.h2.port, /application/db.h2.username.
   * Create 1 Key Vault for password reference with the appropriate name.
   * Create 1 Key Vault Reference in the App Configuration with name /application/db.h2.password and choose the Key Vault created for this reference.
   * Create 1 Key Value pair in App Configuration named RefreshConfigVersion and set the value to 1. This is for auto update all configs for application when the value for this config would be changed.
10. Create secret "secret-property-name" in Key Vault give any value.
11. Create the environment variables in the deployed web app manually like: AZURE_APP_CONFIGURAITON_ENDPOINT, ENV_STAGE_LABEL, AZURE_KEY_VAULT_URL, WEBSITES_PORT.
    * AZURE_APP_CONFIGURAITON_ENDPOINT is app configuration endpoint value that available from the Overview page of the App Configuration service. 
    * ENV_STAGE_LABEL is you environment value like (dev, qa, prod, local ...)
    * AZURE_KEY_VAULT_URL is the Key Vault URL available from the Key Vault Service page.
    * AZURE_KEY_VAULT_SECRET_NAME is the name of the secret created in the step 10. "secret-property-name".
    * WEBSITES_PORT needed if the app exposes the port different from the 80 and is the env variable for azure web app to know the port that it expose and on which the app is running to create the mapping.
12. In the App Services choose the web app, and click on the identity. Enable the status to ON. Add role  Key Vault Secrets User assigment to Key Vault and choose the vault from the dropdown.
13. In the App Configuration add role assigment for App Service web app using the App Configuration Data Reader role to the web app.
14. Restart the Web App and test the endpoints using Default domain as host from the Overview tab in this web app.


For local running and testing using User-Managed Identity
If needed add role assigment for specific resources.
1. Copy the client_id from Managed Identities, click to the resource with specific resource group and copy the client id.
2. in local yaml file add this props and change the AZURE_MANAGED_IDENTITY_CLIENT_ID to the client id value.
```properties
spring:
  cloud:
    azure:
      credential:
        managed-identity-enabled: false
        client-id: ${AZURE_MANAGED_IDENTITY_CLIENT_ID}
```


For local running and testing using props from App Registration
1. Create App Registrations in the Microsoft Entra ID and copy the applicaiton id (client_id)
2. Create a secret in the App Registrations for this client and copy the secret value for future use.
3. Create role assign for this application client in the Key Vault Access Control (IAM) with the role "Key Vault Certificate User" using Service principle (User, Group, ...).
4. Create role assign for this application client in the App Configuration Access Control (IAM) with the role "App Configuration Data Reader" using Service principle (User, Group, ...).
5. Setup environment variables:
    * AZURE_TENANT_ID - copy the value from created App Registrations client.
    * AZURE_CLIENT_ID - copy the value from created App Registrations client.
    * AZURE_CLIENT_SECRET - enter previously copied value from created Secret in App Registrations client.
    * AZURE_KEY_VAULT_URL - copy endpoint from Key Vault page.
    * AZURE_KEY_VAULT_SECRET_NAME - copy the name of the secret created in App Configuration.
    * AZURE_APP_CONFIGURAITON_ENDPOINT - copy the endpoint from App Configuration page.
    * ENV_STAGE_LABEL - set it to local or (dev, qa, prod).
6. Start the application and test it with localhost host.

## Deploy from local steps

Install the AZURE CLI

```bash
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
```

## Login to AZURE CLI (AZ)

```bash
az login
```

## Login to ACR

```bash
az acr login -n [AZURE_REGISTRY_NAME]
```


## Tag teh docker image before pushing it to azure container registry
```bash
docker tag azure_web_app_demo:0.0.1 [REGISTRY_NAME]-[HASH_CREATED_BY_AZURE_ACR].azurecr.io/azure_web_app_demo:0.0.1

```
or if no hash was created for the URL
```bash
docker tag azure_web_app_demo:0.0.1 [REGISTRY_NAME].azurecr.io/azure_web_app_demo:0.0.1

```


## Push the docker image to ACR
```bash
docker image push [AZURE_REGISTRY_NAME]-[HASH_CREATED_BY_AZURE_ACR].azurecr.io/azure_web_app_demo:0.0.1

```


## Need to login to other ACR, first logout from existing account

```bash
az acr logout
```

## Docs
[Spring Cloud for Azure starter App Configuration client library for Java](https://github.com/Azure/azure-sdk-for-java/blob/main/sdk/spring/spring-cloud-azure-starter-appconfiguration-config/README.md)



