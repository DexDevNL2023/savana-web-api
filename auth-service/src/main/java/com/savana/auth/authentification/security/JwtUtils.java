package com.savana.auth.authentification.security;

import com.nimbusds.jose.util.Base64;
import com.savana.auth.authentification.entities.Account;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.expiration}")
    private static final long JWT_EXPIRATION_MS = 86400000;
    @Value("${app.jwt.secret}")
    private static final String JWT_SECRET_KEY = "NWMxZTQzYjM2OGExY2ZjYmU3OTMzZmUyMTI2MjVjNDM3NTYyNzc3MDA0ZmFmNjFlZWQ3YmZhYWVhNDAxZjFlMTIwMjdlZDY4MjkwYjhmM2MzNDQyODM5MGE4MzVhNTg2NDk2ZTA4ZmQzZTNlYzU0NGNjN2Y0MjgwY2Q0NmM5YTk=";

    // Generate a SecretKey from the jwtSecret
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(JWT_SECRET_KEY).decode();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // generate JWT token
    public String generateJwtTokens(Account user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey()) // Use the generated key
                //.signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .claim("id", user.getId())
                .claim("roles", user.getAuthorities())
                .compact();
    }
}
