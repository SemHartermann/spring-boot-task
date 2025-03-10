package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.UserResponseDto;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserService userService;
    Map<String, UserResponseDto> authenticatedUsers = new HashMap<>();
    MeterRegistry meterRegistry;

    @Override
    public UserResponseDto authenticate(String username, String password) throws UserPrincipalNotFoundException {
        log.trace("Authenticating user with username: {}", username);

        Counter counter = Counter.builder("api_endpoint_authenticate")
                .tag("username", username)
                .description("Number of Requests for authenticate")
                .register(meterRegistry);
        counter.increment();

        UserResponseDto userResponseDto = userService.getUserByUsername(username);

        if (userResponseDto.getPassword().equals(password)) {
            authenticatedUsers.put(username, userResponseDto);
        } else {
            log.warn("Authentication failed for username: {}", username);
            throw new UserPrincipalNotFoundException("Wrong password for username: " + username);
        }

        return userResponseDto;
    }

    @Override
    public boolean isAuthenticated(String username) {
        return authenticatedUsers.containsKey(username);
    }

    @Override
    public boolean isActive(String username) throws UserPrincipalNotFoundException {
        return userService.checkIsActive(username);
    }

    @Override
    public void logout(String username) {
        authenticatedUsers.remove(username);
    }
}