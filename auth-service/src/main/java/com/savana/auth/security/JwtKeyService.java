package com.savana.auth.security;

import org.springframework.stereotype.Service;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtKeyService {

    private final KeyPair keyPair;

    public JwtKeyService() {
        this.keyPair = generateKeyPair();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors de la génération des clés RSA", e);
        }
    }

    public RSAPublicKey getPublicKey() {
        return (RSAPublicKey) keyPair.getPublic();
    }

    public String getPublicKeyBase64() {
        return Base64.getEncoder().encodeToString(getPublicKey().getEncoded());
    }

    public Map<String, String> getJwkSet() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        Map<String, String> jwk = new HashMap<>();
        jwk.put("kty", "RSA");
        jwk.put("alg", "RS256");
        jwk.put("use", "sig");
        jwk.put("n", Base64.getUrlEncoder().encodeToString(publicKey.getModulus().toByteArray()));
        jwk.put("e", Base64.getUrlEncoder().encodeToString(publicKey.getPublicExponent().toByteArray()));
        return jwk;
    }
}
