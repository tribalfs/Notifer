package de.dlyt.yanndroid.notifer.utils;

import android.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    public static String generateSecretKeyB64() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey key = kg.generateKey();
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static Pair<String, String> encrypt(String plaintext, String secretKeyBase64) throws GeneralSecurityException {
        SecretKeySpec key = new SecretKeySpec(Base64.getDecoder().decode(secretKeyBase64), "AES");

        byte[] iv = new byte[12];
        SecureRandom.getInstanceStrong().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] ciphertext = cipher.doFinal(
                plaintext.getBytes(StandardCharsets.UTF_8)
        );

        return new Pair<>(
                Base64.getEncoder().encodeToString(iv),
                Base64.getEncoder().encodeToString(ciphertext)
        );
    }
}
