package com.apiPersistence.dataPersistenceApi.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApi(ExternalApiException ex) {
        return ResponseEntity.status(502)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ProfileNotFoundException ex) {
        return ResponseEntity.status(404)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleBadRequest(InvalidRequestException ex) {
        return ResponseEntity.status(400)
                .body(Map.of("status", "error", "message", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(422)
                .body(Map.of("status", "error", "message", "Invalid type for parameter: " + ex.getName()));
    }
}
