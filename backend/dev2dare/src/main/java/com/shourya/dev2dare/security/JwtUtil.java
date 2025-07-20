package com.shourya.dev2dare.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final long JWT_EXPIRATION = 24 * 60 * 60 * 1000; // 24 hours

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String email, String role) {
        logger.info("[JwtUtil] Generating token for email='{}', role='{}'", email, role);
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role); // e.g., COLLEGE (not prefixed here)

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("[JwtUtil] Token generated for email='{}'", email);
        return token;
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            logger.error("[JwtUtil] Missing or invalid Authorization header");
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = header.substring(7);
        logger.debug("[JwtUtil] Extracted JWT from request for header: {}", header);
        return token;
    }

    public String extractEmail(String token) {
        String email = extractAllClaims(token).getSubject();
        logger.debug("[JwtUtil] Extracted email from token: {}", email);
        return email;
    }

    public String extractRole(String token) {
        String role = extractAllClaims(token).get("role", String.class);
        logger.debug("[JwtUtil] Extracted role from token: {}", role);
        return role;
    }

    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            logger.debug("[JwtUtil] Token is valid");
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("[JwtUtil] Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
