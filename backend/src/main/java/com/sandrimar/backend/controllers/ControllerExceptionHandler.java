package com.sandrimar.backend.controllers;

import com.sandrimar.backend.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> invalidParameters(MethodArgumentNotValidException e, HttpServletRequest request) {
        String err = "Invalid Parameters";
        StandardError error = new StandardError(LocalDateTime.now(), err,
                e.getFieldError().getDefaultMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardError> illegalState(IllegalStateException e, HttpServletRequest request) {
        String err = "Illegal state";
        StandardError error = new StandardError(LocalDateTime.now(), err,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> notFound(ResourceNotFoundException e, HttpServletRequest request) {
        String err = "Resource Not Found";
        StandardError error = new StandardError(LocalDateTime.now(), err,
                e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
