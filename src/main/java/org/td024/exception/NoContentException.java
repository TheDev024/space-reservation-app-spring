package org.td024.exception;

import org.springframework.http.HttpStatus;

public class NoContentException extends CustomException {
    public NoContentException(String message) {
        super(message, HttpStatus.NO_CONTENT);
    }
}
