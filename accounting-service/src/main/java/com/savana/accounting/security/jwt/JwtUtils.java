package com.savana.accounting.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final long EXPIRATION_TIME = 86400000; // 24 heures
    private static final String SECRET_KEY = "NWMxZTQzYjM2OGExY2ZjYmU3OTMzZmUyMTI2MjVjNDM3NTYyNzc3MDA0ZmFmNjFlZWQ3YmZhYWVhNDAxZjFlMTIwMjdlZDY4MjkwYjhmM2MzNDQyODM5MGE4MzVhNTg2NDk2ZTA4ZmQzZTNlYzU0NGNjN2Y0MjgwY2Q0NmM5YTk=";

    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean isTokenValid(String token, String username) {
        String tokenUsername = extractUsername(token);
        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private static Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
