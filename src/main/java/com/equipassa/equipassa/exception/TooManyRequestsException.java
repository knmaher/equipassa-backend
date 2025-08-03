package com.equipassa.equipassa.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(final String message) {
        super(message);
    }
}
