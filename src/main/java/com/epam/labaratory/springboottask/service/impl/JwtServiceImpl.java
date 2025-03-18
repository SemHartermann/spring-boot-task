package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.config.properties.JwtProperties;
import com.epam.labaratory.springboottask.dto.AuthenticationRequestDto;
import com.epam.labaratory.springboottask.service.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements JwtService {
    JwtProperties jwtProperties;

    Set<String> invalidatedTokens = new HashSet<>();

    public String generateAccessToken(AuthenticationRequestDto user) {
        return buildToken(user, jwtProperties.getAccessTokenExpiration());
    }

    private String buildToken(AuthenticationRequestDto user, Long expiration) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("username", user.getUsername());

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtProperties.getSecretKey())
        );
    }

    public String extractUsername(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    @Override
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    @Override
    public boolean isTokenInvalided(String token) {
        return invalidatedTokens.contains(token);
    }
}