package com.project.apointment_service.exceptions;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .toList();

        return createResponse(HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<APIError> handleConstraintException(ConstraintViolationException ex){
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();

        return createResponse(HttpStatus.BAD_REQUEST, errors);
    }
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<APIError> handleClientError(HttpClientErrorException ex){
        return createResponse(HttpStatus.valueOf(ex.getStatusCode().value()), List.of(ex.getMessage(), ex.getResponseBodyAsString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIError> handleOtherException(Exception ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<APIError> handleIllegalState(IllegalStateException ex) {
        return createResponse(HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    private ResponseEntity<APIError> createResponse(HttpStatus status, List<String> errors) {
        APIError apiError = new APIError(status, errors, LocalDateTime.now());
        log.error("Error occurred {}", apiError);
        return new ResponseEntity<>(apiError, status);
    }


}
