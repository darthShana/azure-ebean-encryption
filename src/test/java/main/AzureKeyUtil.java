package main;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.KeyClient;
import com.azure.security.keyvault.keys.KeyClientBuilder;
import com.azure.security.keyvault.keys.models.CreateRsaKeyOptions;
import com.azure.security.keyvault.keys.models.KeyVaultKey;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Properties;


public class AzureKeyUtil {

    public static void main(String[] args) throws IOException {
        initialiseSystemProperties();

        // authenticate with client secret,
        ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(System.getProperty("CLIENT_ID"))
                .clientSecret(System.getProperty("CLIENT_SECRET"))
                .tenantId(System.getProperty("TENANT_ID"))
                .build();

        KeyClient keyClient = new KeyClientBuilder()
                .vaultUrl(System.getProperty("VAULT_URL"))
        .credential(clientSecretCredential)
                .buildClient();

        KeyVaultKey rsaKey = keyClient.createRsaKey(new CreateRsaKeyOptions("KeyManagementKey1")
                .setExpiresOn(OffsetDateTime.now().plusYears(1))
                .setKeySize(2048));
        System.out.printf("Key is created with name %s and id %s \n", rsaKey.getName(), rsaKey.getId());
    }

    private static void initialiseSystemProperties() throws IOException {
        Properties p = new Properties();
        p.load(Objects.requireNonNull(AzureKeyUtil.class.getClassLoader().getResourceAsStream("app.properties")));
        for (String name : p.stringPropertyNames()) {
            String value = p.getProperty(name);
            System.setProperty(name, value);
        }
    }
}
