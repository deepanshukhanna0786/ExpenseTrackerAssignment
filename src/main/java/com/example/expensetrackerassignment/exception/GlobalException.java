package com.example.expensetrackerassignment.exception;

import com.example.expensetrackerassignment.constants.ApplicationConstants;
import com.example.expensetrackerassignment.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalException {

    // Handle custom exception for expense not found
    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<ResponseDto<String>> handleExpenseNotFoundException(ExpenseNotFoundException ex, HttpServletRequest request) {
        log.error("ExpenseNotFoundException in request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                HttpStatus.NOT_FOUND,
                request.getRequestURI()
        );
    }

    // Handle invalid data while creating/updating an expense
    @ExceptionHandler(InvalidExpenseDataException.class)
    public ResponseEntity<ResponseDto<String>> handleInvalidExpenseDataException(InvalidExpenseDataException ex, HttpServletRequest request) {
        log.error("InvalidExpenseDataException in request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    // Handle unauthorized access exceptions (e.g., user not authorized to access expenses)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseDto<String>> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.error("UnauthorizedException in request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED,
                request.getRequestURI()
        );
    }

    // Handle validation errors (e.g., invalid input data when creating or updating an expense)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation error in request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Validation Error");
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );
    }

    // Handle any other unexpected generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<String>> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception in request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Technical Error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "Failure");
        body.put("statusCode", HttpStatus.UNAUTHORIZED.value());
        body.put("message", ex.getMessage());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseDto<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied for request {}: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                HttpStatus.FORBIDDEN,
                request.getRequestURI()
        );
    }


    // Helper method to build error response
    private ResponseEntity<ResponseDto<String>> buildErrorResponse(int code, String message, HttpStatus status, String path) {
        ResponseDto<String> response = ResponseDto.<String>builder()
                .status(ApplicationConstants.FAILURE)
                .statusCode(code)
                .message(message)
                .response(null)
                .build();
        return new ResponseEntity<>(response, status);
    }
}
