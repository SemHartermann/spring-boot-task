package com.epam.labaratory.springboottask.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRANSACTION_ID = "transactionId";

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public ResponseEntity<String> handleUserPrincipalNotFoundException(UserPrincipalNotFoundException ex, HttpServletRequest request) {
        String transactionId = MDC.get(TRANSACTION_ID);
        log.error("Transaction ID: {}, Method: {}, URI: {}, Error: {}",
                transactionId, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        String transactionId = MDC.get(TRANSACTION_ID);
        log.error("Transaction ID: {}, Method: {}, URI: {}, Error: {}",
                transactionId, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleExceptions(Exception ex, HttpServletRequest request) {
        String transactionId = MDC.get(TRANSACTION_ID);
        log.error("Transaction ID: {}, Method: {}, URI: {}, Error: {}",
                transactionId, request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}