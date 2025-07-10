package org.td024.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.td024.exception.CustomException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicate(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {
        return generateErrorDetails(e, request, HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {
        Map<String, Object> errorDetails = new java.util.HashMap<>(Map.of());

        Map<String, List<String>> fieldErrorMessages = getFieldErrorMessages(e);

        errorDetails.put("message", "Validation Failed");
        errorDetails.put("errors", fieldErrorMessages);
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("timestamp", new Date());
        errorDetails.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorDetails.put("path", request.getRequestURI());

        return errorDetails;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        HttpStatus status = e.getStatus();
        Map<String, Object> errorDetails = generateErrorDetails(e, request, status);
        return ResponseEntity.status(status).body(errorDetails);
    }

    private Map<String, List<String>> getFieldErrorMessages(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        Map<String, List<String>> fieldErrorMessages = new HashMap<>();

        fieldErrors.forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            if (fieldErrorMessages.containsKey(fieldName)) {
                fieldErrorMessages.get(fieldName).add(errorMessage);
            } else {
                fieldErrorMessages.put(fieldName, Stream.of(errorMessage).collect(Collectors.toList()));
            }
        });
        return fieldErrorMessages;
    }

    private Map<String, Object> generateErrorDetails(Exception e, HttpServletRequest request, HttpStatus status) {
        Map<String, Object> errorDetails = new java.util.HashMap<>(Map.of());

        errorDetails.put("message", e.getLocalizedMessage());
        errorDetails.put("status", status.value());
        errorDetails.put("timestamp", new Date());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("path", request.getRequestURI());

        return errorDetails;
    }
}
