package com.epam.labaratory.springboottask.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionIdFilter implements Filter {

    private static final String TRANSACTION_ID = "transactionId";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String transactionId = UUID.randomUUID().toString();
        MDC.put(TRANSACTION_ID, transactionId);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader(TRANSACTION_ID, transactionId);

        try {
            chain.doFilter(request, response);
        } finally {
            logRequestAndResponse(httpRequest, httpResponse);
            MDC.remove(TRANSACTION_ID);
        }
    }

    private void logRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        String transactionId = MDC.get(TRANSACTION_ID);
        log.info("Transaction ID: {}, Method: {}, URI: {}, Response Status: {}",
                transactionId, request.getMethod(), request.getRequestURI(), response.getStatus());
    }

    @Override
    public void destroy() {
    }
}