package com.savana.auth.security.jwt;

import com.savana.auth.authentification.entities.Account;
import com.savana.auth.generic.utils.AppConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${spring.security.jwt.expiration}")
    private long JWT_EXPIRATION_MS;
    @Value("${spring.security.jwt.secret}")
    private String JWT_SECRET_KEY;

    // Generate a SecretKey from the jwtSecret
    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET_KEY);
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    // parse JWT token from request
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AppConstants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AppConstants.HEADER_PREFIX)) {
            return bearerToken.substring(AppConstants.HEADER_PREFIX.length());
        }
        return null;
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

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
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

    public String refreshJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

            return Jwts.builder()
                    .setSubject(claims.getSubject())
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .addClaims(claims)
                    .signWith(getSecretKey())
                    .compact();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Cannot refresh an expired token");
        }
    }
}
