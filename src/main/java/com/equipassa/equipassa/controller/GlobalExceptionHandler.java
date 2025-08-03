package com.equipassa.equipassa.controller;

import com.equipassa.equipassa.exception.EmailExistsException;
import com.equipassa.equipassa.exception.OrganizationExistsException;
import com.equipassa.equipassa.exception.TooManyRequestsException;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(@Qualifier("messageSource") final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(OrganizationExistsException.class)
    public ResponseEntity<Map<String, Object>> handleOrganizationExistsException(final OrganizationExistsException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> errorResponse = new HashMap<>();
        final String error = messageSource.getMessage("error.organizationNameExists", null, locale);
        errorResponse.put("error", error);
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailExistsException(final EmailExistsException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> errorResponse = new HashMap<>();
        final String error = messageSource.getMessage("error.emailExists", null, locale);
        errorResponse.put("error", error);
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(final BadCredentialsException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> errorResponse = new HashMap<>();
        final String errorMessage = messageSource.getMessage("error.badCredentials", null, locale);
        errorResponse.put("error", errorMessage);
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(final MethodArgumentNotValidException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            final String localizedMessage = messageSource.getMessage(error, locale);
            errors.put(error.getField(), localizedMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(final Exception ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> errorResponse = new HashMap<>();
        final String errorMessage = messageSource.getMessage("error.internal", null, locale);
        errorResponse.put("error", errorMessage);
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TooManyRequestsException.class)
    public ResponseEntity<Map<String, Object>> handleTooManyRequests(final TooManyRequestsException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> body = new HashMap<>();
        final String errorMessage = messageSource.getMessage("error.tooManyRequests", null, locale);
        body.put("error", errorMessage);
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(final BadRequestException ex, final WebRequest request) {
        final Locale locale = request.getLocale();
        final Map<String, Object> body = new HashMap<>();
        final String msg = messageSource.getMessage("error.badRequest", null, locale);
        body.put("error", msg);
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
