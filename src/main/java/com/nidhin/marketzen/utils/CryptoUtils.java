package com.nidhin.marketzen.utils;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.util.Base64;
import java.security.SecureRandom;

@Service
public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "sEPT/XZ12Jj+uRyZWfJuZA==";

    private static SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec key = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Generate a random IV for encryption
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

        // Concatenate IV and ciphertext with a colon separator
        String ivEncoded = Base64.getEncoder().encodeToString(iv);
        String encryptedDataEncoded = Base64.getEncoder().encodeToString(encryptedData);
        return ivEncoded + ":" + encryptedDataEncoded;
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Split the encrypted data into IV and ciphertext parts
        String[] parts = encryptedData.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid encrypted data format");

        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] decodedData = Base64.getDecoder().decode(parts[1]);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return stripQuotes(new String(decryptedData, "UTF-8"));
    }

    // Helper method to strip leading and trailing quotes, if present
    private static String stripQuotes(String input) {
        if (input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }




// If String removal from the decrypted data is not needed use the below code or else use the above code:
/*public class CryptoUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY = "sEPT/XZ12Jj+uRyZWfJuZA==";

    private static SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec key = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Random IV for encryption
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));

        // Concatenate IV and ciphertext
        String ivEncoded = Base64.getEncoder().encodeToString(iv);
        String encryptedDataEncoded = Base64.getEncoder().encodeToString(encryptedData);
        return ivEncoded + ":" + encryptedDataEncoded;
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec key = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        // Split encrypted data into IV and ciphertext
        String[] parts = encryptedData.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid encrypted data format");

        byte[] iv = Base64.getDecoder().decode(parts[0]);
        byte[] decodedData = Base64.getDecoder().decode(parts[1]);
        IvParameterSpec ivParams = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData, "UTF-8");
    }*/
}
