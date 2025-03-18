package com.epam.labaratory.springboottask.service.impl;

import com.epam.labaratory.springboottask.dto.AuthenticationRequestDto;
import com.epam.labaratory.springboottask.dto.AuthenticationResponseDto;
import com.epam.labaratory.springboottask.dto.UserResponseDto;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.JwtService;
import com.epam.labaratory.springboottask.service.LoginAttemptService;
import com.epam.labaratory.springboottask.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserService userService;
    MeterRegistry meterRegistry;
    PasswordEncoder passwordEncoder;
    JwtService jwtService;
    LoginAttemptService loginAttemptService;

    @Override
    public void authenticate(UserDetails user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                user.getAuthorities());

        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto user) throws BadCredentialsException {
        log.trace("Authenticating user with username: {}", user.getUsername());

        Counter counter = Counter.builder("api_endpoint_authenticate")
                .tag("username", user.getUsername())
                .description("Number of Requests for authenticate")
                .register(meterRegistry);
        counter.increment();

        if (loginAttemptService.isBlocked(user.getUsername())) {
            LocalDateTime unlockTime = loginAttemptService.getUnlockTime(user.getUsername());
            throw new BadCredentialsException("User is locked. Try again at " + unlockTime);
        }

        UserResponseDto userResponseDto = userService.getUserByUsername(user.getUsername());

        AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();

        if (passwordEncoder.matches(user.getPassword(), userResponseDto.getPassword())) {
            loginAttemptService.loginSucceeded(user.getUsername());

            authenticationResponseDto.setAccessToken(jwtService.generateAccessToken(user));

            return authenticationResponseDto;
        } else {
            log.warn("Authentication failed for username: {}", user.getUsername());

            loginAttemptService.loginFailed(user.getUsername());

            throw new BadCredentialsException("Wrong password for username: " + user.getUsername());
        }
    }

    @Override
    public boolean isActive(String username) throws UserPrincipalNotFoundException {
        return userService.checkIsActive(username);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            jwtService.invalidateToken(jwt);
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
    }
}