package com.savana.proxy.security;

import com.nimbusds.jose.util.Base64;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.expiration}")
    private static final long JWT_EXPIRATION_MS = 86400000;
    @Value("${app.jwt.secret}")
    private static final String JWT_SECRET_KEY = "NWMxZTQzYjM2OGExY2ZjYmU3OTMzZmUyMTI2MjVjNDM3NTYyNzc3MDA0ZmFmNjFlZWQ3YmZhYWVhNDAxZjFlMTIwMjdlZDY4MjkwYjhmM2MzNDQyODM5MGE4MzVhNTg2NDk2ZTA4ZmQzZTNlYzU0NGNjN2Y0MjgwY2Q0NmM5YTk=";

    // Generate a SecretKey from the jwtSecret
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(JWT_SECRET_KEY).decode();
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }

    private boolean isTokenExpired(String token) {
        return this.getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public boolean isInvalid(String token) {
        return this.isTokenExpired(token);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
