package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.AuthenticationRequestDto;
import io.jsonwebtoken.JwtException;

public interface JwtService {
    String extractUsername(String token) throws JwtException;

    String generateAccessToken(AuthenticationRequestDto user);

    void invalidateToken(String token);

    boolean isTokenInvalided(String token);
}
