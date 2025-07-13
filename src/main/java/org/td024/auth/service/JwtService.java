package org.td024.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.td024.auth.model.TokenPayload;
import org.td024.exception.GoneException;
import org.td024.exception.UnauthorizedException;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final ObjectMapper objectMapper;
    @Value("${org.td024.auth.jwt-secret}")
    private String secret;

    public JwtService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(TokenPayload payload, Long expirationSeconds) throws JsonProcessingException {
        String subject = objectMapper.writeValueAsString(payload);
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + expirationSeconds * 1000);

        return Jwts.builder().subject(subject).issuedAt(issuedAt).expiration(expiresAt).signWith(getKey(), Jwts.SIG.HS256).compact();
    }

    public TokenPayload getPayload(String token) throws JsonProcessingException {
        String subject = getSubject(token);
        return objectMapper.readValue(subject, TokenPayload.class);
    }

    public void validate(String token) {
        try {
            Jwts.parser().decryptWith(getKey()).verifyWith(getKey()).build().parse(token);
        } catch (ExpiredJwtException e) {
            throw new GoneException("Session Expired");
        } catch (MalformedJwtException | SecurityException | IllegalArgumentException e) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    public String getSubject(String token) {
        return Jwts.parser().decryptWith(getKey()).verifyWith(getKey()).build().parseSignedClaims(token).getPayload().getSubject();
    }
}
