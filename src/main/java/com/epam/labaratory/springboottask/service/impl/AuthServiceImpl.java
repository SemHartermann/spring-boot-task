package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.entity.User;
import com.epam.labaratory.springboottask.repository.UserRepository;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    UserRepository userRepository;
    UserService userService;
    ConversionService conversionService;
    Map<String, User> authenticatedUsers = new HashMap<>();

    public Optional<User> authenticate(String username, String password) throws UserPrincipalNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameAndPassword(username, password);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            authenticatedUsers.put(username, user);
        } else {
            throw new UserPrincipalNotFoundException("User not found with username: " + username);
        }

        return userOptional;
    }

    public boolean isAuthenticated(String username) {
        return authenticatedUsers.containsKey(username);
    }

    public boolean isActive(String username) throws UserPrincipalNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserPrincipalNotFoundException("User not found with username: " + username))
                .getIsActive();
    }

    public void logout(String username) {
        authenticatedUsers.remove(username);
    }
}