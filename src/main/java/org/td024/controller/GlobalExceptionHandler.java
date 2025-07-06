package org.td024.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.td024.exception.CustomException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.Map;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDuplicate(SQLIntegrityConstraintViolationException e, HttpServletRequest request) {
        return generateErrorDetails(e, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        HttpStatus status = e.getStatus();
        Map<String, Object> errorDetails = generateErrorDetails(e, request, status);
        return ResponseEntity.status(status).body(errorDetails);
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
