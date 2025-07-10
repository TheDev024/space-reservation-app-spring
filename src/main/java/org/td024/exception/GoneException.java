package org.td024.exception;

import org.springframework.http.HttpStatus;

public class GoneException extends CustomException {
    public GoneException(String message) {
        super(message, HttpStatus.GONE);
    }
}
