package com.epam.labaratory.springboottask.filter;

import com.epam.labaratory.springboottask.filter.utils.EndpointMaster;
import com.epam.labaratory.springboottask.service.AuthService;
import com.epam.labaratory.springboottask.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    JwtService jwtService;

    AuthService authenticationService;

    EndpointMaster endpointMaster;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return endpointMaster.isEndpointMatchedWithPattern(request,
                "/api/trainers/login",
                "/api/trainers/register",
                "/api/trainees/login",
                "/api/trainees/register",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/actuator/**");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (endpointMaster.isHandlerNotExist(request)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);

            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing auth token");

            return;
        }

        String jwt = authHeader.split(" ")[1].trim();

        if (jwtService.isTokenInvalided(jwt)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token was invalidated");

            return;
        }

        try {
            User user = new User(jwtService.extractUsername(jwt), "1", List.of());

            authenticationService.authenticate(user, request);
        } catch (JwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid access token");

            return;
        }

        filterChain.doFilter(request, response);
    }
}
