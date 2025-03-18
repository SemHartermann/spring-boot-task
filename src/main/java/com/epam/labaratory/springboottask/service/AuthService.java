package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.AuthenticationRequestDto;
import com.epam.labaratory.springboottask.dto.AuthenticationResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface AuthService {
    void authenticate(UserDetails user, HttpServletRequest request);

    AuthenticationResponseDto login(AuthenticationRequestDto user) throws BadCredentialsException;

    boolean isActive(String username) throws UserPrincipalNotFoundException;

    void logout(HttpServletRequest request, HttpServletResponse response);
}
