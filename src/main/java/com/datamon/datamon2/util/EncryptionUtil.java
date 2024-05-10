package com.datamon.datamon2.util;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptionUtil {
    private final String instance = "AES/CBC/PKCS5Padding";
    private final SecureRandom secureRandom = new SecureRandom();
    private SecretKeySpec sKeySpec;
    private IvParameterSpec ivSpec;

    @Value("${AES.key}")
    private String aes256key;

    public EncryptionUtil() {
        byte[] keyBytes = new byte[16];
        byte[] b = Base64.getEncoder().encode(aes256key.getBytes());
        int len = Math.min(b.length, keyBytes.length);
        System.arraycopy(b, 0, keyBytes, 0, len);
        sKeySpec = new SecretKeySpec(keyBytes, "AES");
        ivSpec = new IvParameterSpec(keyBytes);
    }

    public String AES256encrypt(String str) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(instance);
            c.init(Cipher.ENCRYPT_MODE, sKeySpec, ivSpec);
            byte[] encrypted = c.doFinal(Base64.getEncoder().encode(str.getBytes("UTF-8")));
            String enStr = new String(Base64.getEncoder().encode(encrypted));
            return enStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String AES256decrypt(String str) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(instance);
            c.init(Cipher.DECRYPT_MODE, sKeySpec, ivSpec);
            byte[] byteStr = Base64.getDecoder().decode(str.getBytes());
            String decStr = new String(c.doFinal(byteStr), "UTF-8");
            return decStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSalt() {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return bytesToHex(salt);
    }

    public String getSHA256WithSalt(String password, String salt) {
        return getRepeatedSHA256(password + salt, 10); // hash password with salt for 10 iterations
    }

    public String getRepeatedSHA256(String input, int iterations) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            for (int i = 1; i < iterations; i++) {
                encodedHash = md.digest(encodedHash);
            }
            return bytesToHex(encodedHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
