package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.dto.UserResponseDto;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface AuthService {
    UserResponseDto authenticate(String username, String password) throws UserPrincipalNotFoundException;

    boolean isAuthenticated(String username);

    boolean isActive(String username) throws UserPrincipalNotFoundException;

    void logout(String username);
}
