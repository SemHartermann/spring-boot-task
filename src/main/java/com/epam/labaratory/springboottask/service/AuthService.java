package com.epam.labaratory.springboottask.service;

import com.epam.labaratory.springboottask.entity.User;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

public interface AuthService {
    Optional<User> authenticate(String username, String password) throws UserPrincipalNotFoundException;

    boolean isAuthenticated(String username);

    boolean isActive(String username) throws UserPrincipalNotFoundException;

    void logout(String username);
}
