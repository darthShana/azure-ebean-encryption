package com.darthShana.encrypt;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.keys.cryptography.CryptographyClient;
import com.azure.security.keyvault.keys.cryptography.CryptographyClientBuilder;
import com.azure.security.keyvault.keys.cryptography.models.DecryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptResult;
import com.azure.security.keyvault.keys.cryptography.models.EncryptionAlgorithm;
import com.darthShana.model.DataEncryptionKey;
import com.darthShana.model.query.QDataEncryptionKey;
import io.ebean.config.EncryptKey;
import io.ebean.config.EncryptKeyManager;

import java.util.Optional;
import java.util.UUID;

public class AzureEnvelopKeyManager implements EncryptKeyManager {


    private CryptographyClient cryptoClient;
    private ClientSecretCredential clientSecretCredential;

    @Override
    public void initialise() {
        clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(System.getProperty("CLIENT_ID"))
                .clientSecret(System.getProperty("CLIENT_SECRET"))
                .tenantId(System.getProperty("TENANT_ID"))
                .build();

        cryptoClient = new CryptographyClientBuilder()
                .credential(clientSecretCredential)
                .keyIdentifier(System.getProperty("KEK_ID"))
                .buildClient();

    }


    @Override
    public EncryptKey getEncryptKey(String tableName, String columnName) {
        Optional<DataEncryptionKey> hasKey = new QDataEncryptionKey()
                .tableName.eq(tableName)
                .columnName.eq(columnName)
                .findOneOrEmpty();

        if(hasKey.isPresent()){
            return () -> decrypt(hasKey.get().getDataEncryptionKey()).with(cryptoClient);
        }else{
            return () -> newDataEncryptionKey(tableName, columnName);
        }

    }

    private String newDataEncryptionKey(String tableName, String columnName) {
        String dek = UUID.randomUUID().toString();
        DataEncryptionKey dataEncryptionKey = new DataEncryptionKey();
        dataEncryptionKey.setTableName(tableName);
        dataEncryptionKey.setColumnName(columnName);
        dataEncryptionKey.setDataEncryptionKey(encrypt(dek).with(cryptoClient));
        dataEncryptionKey.setKekId(System.getProperty("KEK_ID"));
        dataEncryptionKey.save();
        return dek;
    }

    interface Decrypter {
        String with(CryptographyClient client);
    }

    interface Encryptor {
        byte[] with(CryptographyClient client);
    }

    Encryptor encrypt(String toEncrypt){
        return (client) -> {
            EncryptResult encryptResult = client.encrypt(EncryptionAlgorithm.RSA_OAEP, toEncrypt.getBytes());
            System.out.printf("Returned cipherText size is %d bytes with algorithm %s \n", encryptResult.getCipherText().length, encryptResult.getAlgorithm().toString());
            return encryptResult.getCipherText();
        };
    }

    Decrypter decrypt(byte[] encrypted){
        return (client) -> {
            DecryptResult decryptResult = client.decrypt(EncryptionAlgorithm.RSA_OAEP, encrypted);
            System.out.printf("Returned plainText size is %d bytes \n", decryptResult.getPlainText().length);
            return new String(decryptResult.getPlainText());
        };
    }

    public void verifyDataEncryptionKeys() {


        new QDataEncryptionKey().kekId.notEqualTo(System.getProperty("KEK_ID")).findEach(dek -> {

            CryptographyClient oldCryptoClient = new CryptographyClientBuilder()
                    .credential(clientSecretCredential)
                    .keyIdentifier(dek.getKekId())
                    .buildClient();

            dek.setDataEncryptionKey(
                    encrypt(decrypt(dek.getDataEncryptionKey())
                            .with(oldCryptoClient))
                            .with(cryptoClient));

            dek.setKekId(System.getProperty("KEK_ID"));
            System.out.println("converting key for table:"+dek.getTableName()+" column:"+dek.getColumnName());
            dek.save();
        });
    }
}

