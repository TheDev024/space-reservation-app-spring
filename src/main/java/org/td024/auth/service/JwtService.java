package org.td024.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.td024.exception.GoneException;
import org.td024.exception.UnauthorizedException;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${org.td024.auth.jwt-secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(String subject, Long expirationSeconds) {
        Date issuedAt = new Date();
        Date expiresAt = new Date(System.currentTimeMillis() + expirationSeconds * 1000);

        return Jwts.builder().subject(subject).issuedAt(issuedAt).expiration(expiresAt).signWith(getKey(), Jwts.SIG.HS256).compact();
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
