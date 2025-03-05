package com.epam.labaratory.springboottask.filter;

import com.epam.labaratory.springboottask.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthFilter implements Filter {

    AuthService authService;

    @Override
    public void init(FilterConfig filterConfig){
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();

        if (path.equals("/trainees/login") ||
                path.equals("/trainees/register") ||
                path.equals("/trainers/login") ||
                path.equals("/trainers/register") ||
                path.startsWith("/swagger-ui/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/webjars")) {
            chain.doFilter(request, response);
            return;
        }

        String username = httpRequest.getHeader("Authorization");

        if (username != null) username = username.replace("Bearer ", "");

        if (username != null && authService.isAuthenticated(username)) {
            if (authService.isActive(username) ||
                    path.equals("/trainees/activate") ||
                    path.equals("/trainers/activate")) {
                chain.doFilter(request, response);
            } else {
                httpResponse.getWriter().write("Forbidden: User is not active");
                httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } else {
            httpResponse.getWriter().write("Unauthorized");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {
    }
}